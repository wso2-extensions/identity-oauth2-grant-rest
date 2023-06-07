package org.wso2.carbon.identity.oauth2.grant.mfa.framework;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.extension.identity.emailotp.common.exception.EmailOtpException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.multi.attribute.login.mgt.ResolvedUserResult;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.CacheBackedMFATokenDAO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.MFATokenDAOImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.MFATokenDO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.*;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler.AuthenticationStepExecutorService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler.AuthenticationStepExecutorServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal.MFAAuthServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AuthenticationListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO;
import org.wso2.carbon.identity.smsotp.common.dto.GenerationResponseDTO;
import org.wso2.carbon.identity.smsotp.common.dto.ValidationResponseDTO;
import org.wso2.carbon.identity.smsotp.common.exception.SMSOTPException;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * This class implements the MFAAuthService interface.
 */
public class MFAAuthServiceImpl implements MFAAuthService {
    private static SMSOTPService smsOtpService;
    private static EmailOtpService emailOTPService;
    private static final Log log = LogFactory.getLog(MFAAuthServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public BasicAuthResponseDTO authenticatefirstStep(String username, String password, String clientId,
                                                      String requestTenantDomain)
			throws MFAAuthException {

        String authenticator;

        ConfigsDTO configsDTO = MFAAuthServiceDataHolder.getConfigs();
        String customLocalAuthenticatorName = configsDTO.getCustomLocalAuthenticatorName();

        if(StringUtils.isNotBlank(customLocalAuthenticatorName)){
            authenticator = customLocalAuthenticatorName;
        } else{
            if(StringUtils.isEmpty(password)){
                authenticator = Constants.AUTHENTICATOR_NAME_IDENTIFIER_FIRST;
            } else {
                authenticator = Constants.AUTHENTICATOR_NAME_BASIC_AUTH;
            }
        }

        String userTenantDomain = requestTenantDomain == null ?
                PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain() : requestTenantDomain;

        String resolvedUsername = getResolvedUsername(username, userTenantDomain);
        String fullQualifiedUsername = UserCoreUtil.addTenantDomainToEntry(resolvedUsername, userTenantDomain);
        User user = User.getUserFromUserName(fullQualifiedUsername);

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.BASIC_AUTH_PARAM_USERNAME, fullQualifiedUsername);
        params.put(Constants.BASIC_AUTH_PARAM_CLIENT_ID, clientId);

        AuthenticationContext authContext =
                new AuthenticationContext.Builder(params).authenticator(authenticator).userTenantId(IdentityTenantUtil.getTenantId(userTenantDomain)).buildForBasicAuth();

        authContext.setUserId(getUserIDFromUserName(user.getUserName(), authContext.getUserTenantId()));

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        validateAuthStep(authContext.getAuthenticationSteps(), authenticator);

        authContext.setUser(user);

        executeListeners(authContext, Constants.EVENT_PRE_AUTHENTICATION);
        switch (authenticator) {
            case Constants.AUTHENTICATOR_NAME_IDENTIFIER_FIRST:
                try {
                    if (getUserStoreManager(authContext.getUserTenantId()).isExistingUserWithID(authContext.getUserId())) {
                        getUserStoreManager(authContext.getUserTenantId());
                        updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                    } else {
                        throw Util.handleClientException(
                                Constants.ErrorMessage.CLIENT_INVALID_USER, username);
                    }
                } catch (UserStoreException e) {
                    throw Util.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATE_USER_ERROR,
                            String.format("Error while authenticating the user : %s.", username), e);
                }
                break;
            case Constants.AUTHENTICATOR_NAME_BASIC_AUTH:
                if (validateUserCredentials(resolvedUsername , password, authContext.getUserTenantId())) {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                } else {
                    throw Util.handleClientException(
                            Constants.ErrorMessage.CLIENT_INCORRECT_USER_CREDENTIALS, username);
                }
                break;
        }
        executeListeners(authContext, Constants.EVENT_POST_AUTHENTICATION);

        if ( authContext.getAuthenticationSteps().size() ==  authContext.getAuthenticatedSteps().size()) {
            authContext.setAuthFlowCompleted(true);
        }

        CacheBackedMFATokenDAO.getInstance().addMFATokenData(buildMFATokenDO(authContext));

        return buildBasicAuthResponseDTO(authContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MFAInitializationResponseDTO initializeMFA(String userId, String authenticator, String mfaToken)
			throws MFAAuthException {

        AuthenticationStepExecutorService authenticationStepExecutorService =
                new AuthenticationStepExecutorServiceImpl();

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MFA_INITIALIZE_PARAM_USER_ID, userId);
        params.put(Constants.MFA_INITIALIZE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.MFA_INITIALIZE_PARAM_MFA_TOKEN, mfaToken);

        AuthenticationContext authContext =
                new AuthenticationContext.Builder(params).buildForMFAInitialize();

        MFATokenDO mfaTokenDO = CacheBackedMFATokenDAO.getInstance().getMFATokenData(mfaToken);

        authContext.setServiceProvider(mfaTokenDO.getServiceProviderAppId());
        authContext.setMfaTokenId(mfaTokenDO.getMfaTokenId());
        authContext.setUserTenantId(mfaTokenDO.getUserTenantId());
        authContext.setSpTenantId(mfaTokenDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        if (isValidMfaToken(mfaTokenDO) && validateUser(mfaTokenDO, userId)) {

            validateAuthStep(mfaTokenDO.getAuthenticatedSteps(), authContext.getAuthenticationSteps(), authenticator);

            if (authenticator.equals(Constants.AUTHENTICATOR_NAME_SMSOTP)) {
                GenerationResponseDTO mfaResponseDTO = (GenerationResponseDTO) authenticationStepExecutorService.
                        getAuthenticatorServiceGenerationResponse(getAuthenticatorService(authenticator),
                        userId, IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()));
                authContext.setNewMfaToken(mfaResponseDTO.getTransactionId()).setOtp(mfaResponseDTO.getSmsOTP());

            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO mfaResponseDTO;
                mfaResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceGenerationResponse
                                        (getAuthenticatorService(authenticator), userId,
                                                IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()));
                authContext.setNewMfaToken(mfaResponseDTO.getTransactionId()).setOtp(mfaResponseDTO.getEmailOTP());
            } else {
               throw Util.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
        }

        mfaTokenDO.setMfaTokenId(authContext.getNewMfaTokenId());
        mfaTokenDO.setMfaToken(authContext.getNewMfaToken());

        CacheBackedMFATokenDAO.getInstance().refreshMfaToken(authContext.getMfaTokenId(),mfaToken,
                mfaTokenDO);

        return buildMfaInitializationResponseDTO(authContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MFAValidationResponseDTO validateMFA(String userId, String mfaToken, String authenticator, String otp)
			throws MFAAuthException {

        AuthenticationStepExecutorService authenticationStepExecutorService =
                new AuthenticationStepExecutorServiceImpl();

        MFAValidationResponseDTO mfaValidationResponse = null;
        boolean showFailureReason = MFAAuthServiceDataHolder.getConfigs().isShowFailureReason();

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MFA_VALIDATE_PARAM_USER_ID, userId);
        params.put(Constants.MFA_VALIDATE_PARAM_MFA_TOKEN, mfaToken);
        params.put(Constants.MFA_VALIDATE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.MFA_VALIDATE_PARAM_OTP, otp);

        AuthenticationContext authContext =
                new AuthenticationContext.Builder(params).buildForMFAValidate();

        MFATokenDO mfaTokenDO = CacheBackedMFATokenDAO.getInstance().getMFATokenData(mfaToken);
        User user = User.getUserFromUserName(mfaTokenDO.getFullQualifiedUserName());
        authContext.setUser(user);
        authContext.setServiceProvider(mfaTokenDO.getServiceProviderAppId());
        authContext.setAuthenticatedSteps(mfaTokenDO.getAuthenticatedSteps());
        authContext.setMfaTokenId(mfaTokenDO.getMfaTokenId());
        authContext.setUserTenantId(mfaTokenDO.getUserTenantId());
        authContext.setSpTenantId(mfaTokenDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        executeListeners(authContext, Constants.EVENT_PRE_AUTHENTICATION);

        if (isValidMfaToken(mfaTokenDO) && validateUser(mfaTokenDO, userId)) {

            validateAuthStep(mfaTokenDO.getAuthenticatedSteps(), authContext.getAuthenticationSteps(),
                    authenticator);

            if (authenticator.equals(Constants.AUTHENTICATOR_NAME_SMSOTP)) {
                ValidationResponseDTO mfaResponseDTO;
                mfaResponseDTO =
                        (ValidationResponseDTO) authenticationStepExecutorService.
                                getAuthenticatorServiceValidationResponse(getAuthenticatorService(authenticator),
                        mfaToken, userId, IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()), otp);

                authContext.setValidOtp(mfaResponseDTO.isValid());

                FailureReasonDTO failureReasonDTO = showFailureReason? mfaResponseDTO.getFailureReason(): null;
                mfaValidationResponse = buildMfaValidationResponse(authContext, mfaTokenDO, failureReasonDTO);

            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO mfaResponseDTO;
                mfaResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceValidationResponse
                                        (getAuthenticatorService(authenticator), mfaToken, userId,
                                                IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()), otp);

                authContext.setValidOtp(mfaResponseDTO.isValid());

                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDTO =
                        showFailureReason ?
                        mfaResponseDTO.getFailureReason() : null;
                mfaValidationResponse = buildMfaValidationResponse(authContext, mfaTokenDO, failureReasonDTO);

            } else {
                throw Util.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
        }

        executeListeners(authContext, Constants.EVENT_POST_AUTHENTICATION);

        if (authContext.isValidOtp()) {
            CacheBackedMFATokenDAO.getInstance().refreshMfaToken(authContext.getMfaTokenId(),
                    authContext.getMfaToken(), mfaTokenDO);
            MFATokenDAOImpl.getInstance().addAuthenticatedStep(authContext.getAuthenticatedSteps().size(), authenticator,
                    mfaTokenDO.getMfaTokenId());
        }

        return mfaValidationResponse;
    }

    /**
     * This method returns the resolved username when multi-attribute login is enabled.
     *
     * @param username             username passed in the request
     * @param userTenantDomain     tenant domain of the user
     * @throws MFAAuthException if any server or client error occurred.
     */
    private String getResolvedUsername(String username, String userTenantDomain) throws MFAAuthException {

        if (MFAAuthServiceDataHolder.getInstance().getMultiAttributeLogin().isEnabled(userTenantDomain)) {
            ResolvedUserResult resolvedUserResult = MFAAuthServiceDataHolder.getInstance().getMultiAttributeLogin().
                    resolveUser(username, userTenantDomain);
            if (resolvedUserResult != null && ResolvedUserResult.UserResolvedStatus.SUCCESS.
                    equals(resolvedUserResult.getResolvedStatus())) {
                username = resolvedUserResult.getUser().getUsername();
            } else if (ResolvedUserResult.UserResolvedStatus.FAIL.equals(resolvedUserResult.getResolvedStatus())) {
                if (resolvedUserResult.getErrorMessage()!= null)
                    throw Util.handleClientException(Constants.ErrorMessage.CLIENT_CUSTOM_AUTHENTICATE_USER_ERROR,
                            resolvedUserResult.getErrorMessage());
            } else {
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_INVALID_USER, username);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Multi-Attribute login is not enabled.");
            }
        }

        return username;
    }

    /**
     * This method checks whether cross tenant access is enabled.
     *
     * @param serviceProvider      Service Provider
     * @param userTenantId         user tenant Id
     * @param spTenantId           service provider tenant Id
     * @throws MFAAuthException if any server or client error occurred.
     */
    private void validateCrossTenantAccess(ServiceProvider serviceProvider, int userTenantId, int spTenantId) throws MFAAuthException {

        if(serviceProvider.isSaasApp() == false) {
            if (userTenantId != spTenantId) {
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_USER_SP_TENANT_MISMATCH);
            }
        }

    }

    /**
     * This method executes authentication listeners.
     *
     * @param authContext         User object of the user.
     * @throws MFAAuthException if any server or client error occurred.
     */
    private void executeListeners (AuthenticationContext authContext, String event) throws  MFAAuthException {

            switch (event) {
                case "EVENT_PRE_AUTHENTICATION":
                    for (AuthenticationListener listener : MFAAuthListenerServiceImpl
                            .getAuthenticationListeners()) {
                        listener.doPreAuthenticate(authContext);
                    }
                case "EVENT_POST_AUTHENTICATION":
                    for (AuthenticationListener listener : MFAAuthListenerServiceImpl
                            .getAuthenticationListeners()) {
                        listener.doPostAuthenticate(authContext);
                    }
            }
    }

    /**
     * This method gets the Authenticator Service Instance from OSGi Service.
     *
     * @param authenticatorService Authenticator Service instance.
     * @return service instance.
     */
    private Object getAuthenticatorService (String authenticatorService) {

        Object serviceInstance = null;

        if (authenticatorService.equals(Constants.AUTHENTICATOR_NAME_SMSOTP) && smsOtpService == null) {
            // if SMSOtpService is null get instance from OSGi service.
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(SMSOTPService.class, null);
        } else if (authenticatorService.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP) && emailOTPService == null) {
            // if EmailOTPService is null get instance from OSGi service.
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(EmailOtpService.class, null);
        }

        return serviceInstance;
    }

    /**
     * This method verifies the user to whom the MFA token was issued.
     *
     * @param mfaTokenDO 	MFA Token Data Object.
     * @param userId        provided user Id.
     * @return whether the provided user is the user to whom the MFA token was issued.
     * @throws MFAAuthException if any server or client error occurred.
     */
    public boolean validateUser (MFATokenDO mfaTokenDO, String userId) throws MFAAuthException {

        if (mfaTokenDO.getUserId().equals(userId)) {
            return true;
        } else {
            throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_USERID_TOKEN_MISMATCH, mfaTokenDO.getMfaToken());
        }
    }

    /**
     * This method validates user credentials.
     *
     * @param username 		username of the user.
     * @param password      password of the user.
     * @return whether the user is authorized.
     * @throws MFAAuthException if any server or client error occurred.
     */
    private boolean validateUserCredentials(String username, String password, int userTenantId) throws MFAAuthException {

        String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(username);

        boolean authorized;
        try {
            authorized = getUserStoreManager(userTenantId).authenticate(tenantAwareUserName, password);

        } catch (UserStoreException e) {
            throw Util.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATE_USER_ERROR,
                    String.format("Error while authenticating the user : %s.", username), e);
        }

        return authorized;
    }

    /**
     * This method retrieves the user store manager.
     *
     * @return user store manager.
     * @throws MFAAuthException if any server or client error occurred.
     */
    private AbstractUserStoreManager getUserStoreManager(int userTenantId) throws MFAAuthException {

        UserStoreManager userStoreManager;

        try {
            userStoreManager = (AbstractUserStoreManager) MFAAuthServiceDataHolder.getInstance()
                    .getRealmService().getTenantUserRealm(userTenantId).getUserStoreManager();
        } catch (UserStoreException e) {
            throw Util.handleServerException(Constants.ErrorMessage.SERVER_USER_STORE_MANAGER_ERROR,
                    "Error while retrieving user store manager.", e);
        }

        return (AbstractUserStoreManager) userStoreManager;
    }

    /**
     * This method retrieves user Id using Username.
     *
     * @param username 		username of the user.
     * @return user Id.
     * @throws MFAAuthException if any server or client error occurred.
     */
    public String getUserIDFromUserName(String username, int userTenantId) throws MFAAuthException {

        String userId;
        String tenantAwareUsername = MultitenantUtils.getTenantAwareUsername(username);

        try {
            userId = getUserStoreManager(userTenantId).getUserIDFromUserName(tenantAwareUsername);

        } catch (UserStoreException e) {
            throw Util.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_USER_ID_ERROR,
                    String.format("Error while retrieving userId for the username : %s.", username), e);
        }

        if(userId == null) {
            throw Util.handleClientException(Constants.ErrorMessage.CLIENT_INVALID_USER, username);
        }

        return userId;

    }

    /**
     * This method retrieves the following authentication step.
     *
     * @param authenticatedSteps 		Authentication Steps the user has successfully completed.
     * @param authenticationSteps 		Authentication Steps defined for the Service Provider.
     * @return next authentication step.
     */
    private int fetchNextAuthStep(LinkedHashMap<Integer, String> authenticatedSteps ,
                               LinkedHashMap<Integer, List<String>> authenticationSteps) {

        int nextAuthStep;
        List<Integer> steps = new ArrayList<>(authenticationSteps.keySet());
        if (authenticatedSteps != null && authenticatedSteps.size() == authenticationSteps.size()) {
            nextAuthStep = -1;
        } else if (authenticatedSteps != null) {
            nextAuthStep = steps.get(authenticatedSteps.size());
        } else {
            nextAuthStep = steps.get(0);
        }

        return nextAuthStep;
    }

    /**
     * This method retrieves the following authentication step.
     *
     * @param authenticationSteps 		Authentication Steps defined for the Service Provider.
     * @return next authentication step.
     */
    private int fetchNextAuthStep(LinkedHashMap<Integer, List<String>> authenticationSteps) {

        int nextAuthStep;
        List<Integer> steps = new ArrayList<>(authenticationSteps.keySet());
        nextAuthStep = steps.get(0);
        return nextAuthStep;
    }

    /**
     * This method validates the executed Authentication Step.
     *
     * @param authenticatedSteps 		Authentication Steps the user has successfully completed.
     * @param authenticationSteps 		Authentication Steps defined for the Service Provider.
     * @param authenticator 			Name of the Authenticator.
     * @throws MFAAuthException if any server or client error occurred.
     */
    private void validateAuthStep (LinkedHashMap<Integer, String> authenticatedSteps ,
                                     LinkedHashMap<Integer, List<String>> authenticationSteps, String authenticator)
            throws MFAAuthException {

        int nextAuthStep = fetchNextAuthStep(authenticatedSteps,  authenticationSteps);
        if (nextAuthStep == -1) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_AUTHSTEP_OUT_OF_BOUNDS, authenticator);
        }
        List<String> authenticators = authenticationSteps.get(nextAuthStep);
        if(!authenticators.contains(authenticator)) {
            throw Util.handleClientException(Constants.ErrorMessage.CLIENT_INVALID_AUTHENTICATOR, authenticator);
        }
    }

    /**
     * This method validates the executed Authentication Step.
     *
     * @param authenticationSteps 		Authentication Steps defined for the Service Provider.
     * @param authenticator 			Name of the Authenticator.
     * @throws MFAAuthException if any server or client error occurred.
     */
    private void validateAuthStep (LinkedHashMap<Integer, List<String>> authenticationSteps, String authenticator)
            throws MFAAuthException {

        int nextAuthStep = fetchNextAuthStep(authenticationSteps);
        if (nextAuthStep == -1) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_AUTHSTEP_OUT_OF_BOUNDS, authenticator);
        }
        List<String> authenticators = authenticationSteps.get(nextAuthStep);
        if(!authenticators.contains(authenticator)) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_INVALID_AUTHENTICATOR, authenticator);
        }
    }

    /**
     * This method builds MFA Token Data Object.
     *
     * @param authContext 		Authentication context.
     * @return MFA Token Data Object.
     */
    private MFATokenDO buildMFATokenDO(AuthenticationContext authContext) {

        MFATokenDO mfaTokenDO = new MFATokenDO();

        mfaTokenDO.setUserId(authContext.getUserId());
        mfaTokenDO.setFullQualifiedUserName(User.getUserFromUserName(authContext.getUsername()).toFullQualifiedUsername());
        mfaTokenDO.setMfaTokenId(authContext.getNewMfaTokenId());
        mfaTokenDO.setMfaToken(authContext.getNewMfaToken());
        mfaTokenDO.setMfaTokenState(authContext.getMfaTokenState());
        mfaTokenDO.setGeneratedTime(System.currentTimeMillis());
        mfaTokenDO.setExpiryTime(mfaTokenDO.getGeneratedTime() + authContext.getMfaTokenValidityPeriod());
        mfaTokenDO.setAuthFlowCompleted(authContext.isAuthFlowCompleted());
        mfaTokenDO.setServiceProviderAppId(authContext.getServiceProvider().getApplicationID());
        mfaTokenDO.setAuthenticatedSteps(authContext.getAuthenticatedSteps());
        mfaTokenDO.setSpTenantId(authContext.getSpTenantId());
        mfaTokenDO.setUserTenantId(authContext.getUserTenantId());

        return mfaTokenDO;
    }

    /**
     * This method builds Basic Authentication Response DTO.
     *
     * @param authContext 		Authentication context.
     * @return Basic Authentication Response Data Transfer Object.
     */
    private BasicAuthResponseDTO buildBasicAuthResponseDTO(AuthenticationContext authContext) {

        BasicAuthResponseDTO responseDTO = new BasicAuthResponseDTO();
        responseDTO.setMfaToken(authContext.getNewMfaToken());
        responseDTO.setAuthenticationSteps(authContext.getAuthenticationSteps());
        responseDTO.setAuthenticatedSteps(authContext.getAuthenticatedSteps());
        responseDTO.setAuthFlowCompleted(authContext.isAuthFlowCompleted());
        responseDTO.setNextStep(fetchNextAuthStep(authContext.getAuthenticatedSteps(), authContext.getAuthenticationSteps()));

        return responseDTO;
    }

    /**
     * This method builds MFA Initialization Response DTO.
     *
     * @param authContext 	 Authentication context.
     * @return MFA Initialization Response Data Transfer Object.
     */
    private MFAInitializationResponseDTO buildMfaInitializationResponseDTO(AuthenticationContext authContext) {

        MFAInitializationResponseDTO responseDTO = new MFAInitializationResponseDTO();
        responseDTO.setMfaToken(authContext.getNewMfaToken());
        responseDTO.setAuthenticator(authContext.getAuthenticator());
        responseDTO.setOtp(authContext.getOtp());

        return responseDTO;
    }

    /**
     * This method builds MFA Validation Response.
     *
     * @param authContext           Authentication Context.
     * @param failureReasonDTO      Failure Reason Data Transfer Object.
     * @return MFA Validation Response.
     */
    private MFAValidationResponseDTO buildMfaValidationResponse (AuthenticationContext authContext,
                                                                 MFATokenDO mfaTokenDO,
                                                                 Object failureReasonDTO) {

        MFAValidationResponseDTO mfaValidationResponse = new MFAValidationResponseDTO();
        MFAValidationFailureReasonDTO failureReason = null;

        if (authContext.isValidOtp()) {
            updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), authContext.getAuthenticatedSteps().size() + 1, authContext.getAuthenticator());

            if ( authContext.getAuthenticationSteps().size() ==  authContext.getAuthenticatedSteps().size()) {
                authContext.setAuthFlowCompleted(true);
                mfaTokenDO.setAuthFlowCompleted(true);
            }
            mfaTokenDO.setMfaTokenId( authContext.getNewMfaTokenId());
            mfaTokenDO.setMfaToken( authContext.getNewMfaToken());
        }

        mfaValidationResponse.setValid(authContext.isValidOtp())
                .setUserId(authContext.getUserId())
                .setMfaToken(authContext.getNewMfaToken()).setAuthFlowCompleted(authContext.isAuthFlowCompleted())
                .setAuthenticatedSteps(authContext.getAuthenticatedSteps())
                .setAuthenticationSteps(authContext.getAuthenticationSteps())
                .setNextStep(fetchNextAuthStep(authContext.getAuthenticatedSteps(),
                        authContext.getAuthenticationSteps()));

        if (failureReasonDTO != null) {
            if (failureReasonDTO instanceof FailureReasonDTO) {
                FailureReasonDTO failureReasonDO = (FailureReasonDTO) failureReasonDTO;
                failureReason = new MFAValidationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());
            } else if (failureReasonDTO instanceof org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) {
                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) failureReasonDTO;
                failureReason = new MFAValidationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());
            }
            mfaValidationResponse.setFailureReason(failureReason);
        }
        return mfaValidationResponse;
    }

    /**
     * This method checks whether the provided MFA Token is valid.
     *
     * @param mfaTokenDO 	 MFA Token Data Object.
     * @return whether the MFA token is ACTIVE or not.
     * @throws MFAAuthException if any server or client error occurred.
     */
    public boolean isValidMfaToken (MFATokenDO mfaTokenDO) throws MFAAuthException {

        boolean isValidMfaToken = false;
        int mfaTokenTimestampSkew = MFAAuthServiceDataHolder.getConfigs().getTimestampSkew();

        // Check for expired mfa tokens.
        if (System.currentTimeMillis() > (mfaTokenDO.getExpiryTime() + mfaTokenTimestampSkew)) {
            CacheBackedMFATokenDAO.getInstance().updateMfaTokenState(mfaTokenDO.getMfaToken(),
                    Constants.MFA_TOKEN_STATE_EXPIRED );
            mfaTokenDO.setMfaTokenState(Constants.MFA_TOKEN_STATE_EXPIRED);
        }

        switch (mfaTokenDO.getMfaTokenState()) {
            case Constants.MFA_TOKEN_STATE_ACTIVE:
                isValidMfaToken = true;
                break;
            case Constants.MFA_TOKEN_STATE_INACTIVE:
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_INACTIVE_MFA_TOKEN, mfaTokenDO.getMfaToken());
            case Constants.MFA_TOKEN_STATE_EXPIRED:
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_EXPIRED_MFA_TOKEN, mfaTokenDO.getMfaToken());
        }

        return isValidMfaToken;
    }

    /**
     * This method adds successfully authenticated steps into the authenticatedSteps Object.
     *
     * @param authenticatedSteps 	 Existing authenticatedSteps Object.
     * @param stepNo 	             Successfully authenticated Authentication Step Number.
     * @param authenticator 	     Successfully authenticated Authenticator.
     */
    private void updateAuthenticatedSteps (LinkedHashMap<Integer, String> authenticatedSteps, Integer stepNo,
                                           String authenticator) {

        authenticatedSteps.put(stepNo, authenticator);

    }
}
