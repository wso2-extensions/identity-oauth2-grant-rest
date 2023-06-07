/*
 *  Copyright (c) 2023, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC licenses this file to you under the Apache license,
 *  Version 2.0 (the "license"); you may not use this file except
 *  in compliance with the license.
 *  You may obtain a copy of the license at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.oauth2.grant.mfa.framework;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.multi.attribute.login.mgt.ResolvedUserResult;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.context.AuthRestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.CacheBackedFlowIdDAO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.FlowIdDAOImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.FlowIdDO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationValidationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.SPAuthneticationStepsDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler.AuthenticationStepExecutorService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler.AuthenticationStepExecutorServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal.AuthenticationServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AuthenticationListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO;
import org.wso2.carbon.identity.smsotp.common.dto.GenerationResponseDTO;
import org.wso2.carbon.identity.smsotp.common.dto.ValidationResponseDTO;
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
 * This class implements the AuthenticationAuthService interface.
 */
public class AuthenticationServiceImpl implements AuthenticationAuthService {
    private static SMSOTPService smsOtpService;
    private static EmailOtpService emailOTPService;
    private static final Log log = LogFactory.getLog(AuthenticationServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAuthenticationResponseDTO authenticateWithFlowId(String flowId, String authenticator, String password)
            throws AuthenticationException {

        AuthenticationStepExecutorService authenticationStepExecutorService =
                new AuthenticationStepExecutorServiceImpl();

        UserAuthenticationResponseDTO userAuthenticationResponse = null;
        boolean showFailureReason = AuthenticationServiceDataHolder.getConfigs().isShowFailureReason();

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.VALIDATE_PARAM_FLOW_ID, flowId);
        params.put(Constants.VALIDATE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.VALIDATE_PARAM_PASSWORD, password);

        AuthRestAuthenticationContext authContext =
                new AuthRestAuthenticationContext.Builder(params).buildForAuthValidate();

        FlowIdDO flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);
        authContext.setUserId(flowIdDO.getUserId());

        User user = User.getUserFromUserName(flowIdDO.getFullQualifiedUserName());
        authContext.setUser(user);
        authContext.setServiceProvider(flowIdDO.getServiceProviderAppId());
        authContext.setAuthenticatedSteps(flowIdDO.getAuthenticatedSteps());
        authContext.setFlowIdIdentifier(flowIdDO.getFlowIdIdentifier());
        authContext.setUserTenantId(flowIdDO.getUserTenantId());
        authContext.setSpTenantId(flowIdDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        executeListeners(authContext, Constants.PRE_AUTHENTICATION);

        if (isValidFlowId(flowIdDO) && validateUser(flowIdDO, authContext.getUserId())) {

            validateAuthStep(flowIdDO.getAuthenticatedSteps(), authContext.getAuthenticationSteps(),
                    authenticator);

            if (authenticator.equals(Constants.AUTHENTICATOR_NAME_SMSOTP)) {
                ValidationResponseDTO authResponseDTO;
                authResponseDTO =
                        (ValidationResponseDTO) authenticationStepExecutorService.
                                getAuthenticatorServiceValidationResponse(getAuthenticatorService(authenticator),
                                        flowId, authContext.getUserId(), IdentityTenantUtil
                                                .getTenantDomain(authContext.getUserTenantId()), password);

                authContext.setValidPassword(authResponseDTO.isValid());

                FailureReasonDTO failureReasonDTO = showFailureReason ? authResponseDTO.getFailureReason() : null;
                userAuthenticationResponse = buildAuthValidationResponse(authContext, flowIdDO, failureReasonDTO);

            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO authResponseDTO;
                authResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceValidationResponse
                                        (getAuthenticatorService(authenticator), flowId, authContext.getUserId(),
                                                IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()),
                                                password);

                authContext.setValidPassword(authResponseDTO.isValid());
                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDTO =
                        showFailureReason ?
                                authResponseDTO.getFailureReason() : null;
                userAuthenticationResponse = buildAuthValidationResponse(authContext, flowIdDO, failureReasonDTO);
            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_BASIC_AUTH)) {
                boolean isValidPwd =
                        validateUserCredentials(authContext.getUser().getUserName(), password,
                                authContext.getUserTenantId());
                authContext.setValidPassword(isValidPwd);
                Object failureReasonDTO = null;
                userAuthenticationResponse = buildAuthValidationResponse(authContext, flowIdDO, failureReasonDTO);
            } else {
                throw Util.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
        }

        executeListeners(authContext, Constants.POST_AUTHENTICATION);

        if (authContext.isValidPassword()) {
            CacheBackedFlowIdDAO.getInstance().refreshFlowId(authContext.getFlowIdIdentifier(),
                    authContext.getFlowId(), flowIdDO);
            FlowIdDAOImpl.getInstance().addAuthenticatedStep(authContext.getAuthenticatedSteps().size(), authenticator,
                    flowIdDO.getFlowIdIdentifier());
        }

        return userAuthenticationResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationInitializationResponseDTO
    initializeAuth(String flowId, String authenticator) throws AuthenticationException {

        MultiAttributeLoginService multiAttributeLoginService = AuthenticationServiceDataHolder.
                getInstance().getMultiAttributeLogin();

        AuthenticationStepExecutorService authenticationStepExecutorService =
                new AuthenticationStepExecutorServiceImpl();

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.INITIALIZE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.INITIALIZE_PARAM_FLOW_ID, flowId);

        AuthRestAuthenticationContext authContext =
                new AuthRestAuthenticationContext.Builder(params).buildForAuthInitialize();

        FlowIdDO flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);

        authContext.setUserId(flowIdDO.getUserId());
        authContext.setServiceProvider(flowIdDO.getServiceProviderAppId());
        authContext.setFlowIdIdentifier(flowIdDO.getFlowIdIdentifier());
        authContext.setUserTenantId(flowIdDO.getUserTenantId());
        authContext.setSpTenantId(flowIdDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        if (isValidFlowId(flowIdDO) && validateUser(flowIdDO, authContext.getUserId())) {

            validateAuthStep(flowIdDO.getAuthenticatedSteps(), authContext.getAuthenticationSteps(), authenticator);

            if (authenticator.equals(Constants.AUTHENTICATOR_NAME_SMSOTP)) {
                GenerationResponseDTO authResponseDTO = (GenerationResponseDTO) authenticationStepExecutorService.
                        getAuthenticatorServiceGenerationResponse(getAuthenticatorService(authenticator),
                                authContext.getUserId(), IdentityTenantUtil.getTenantDomain
                                        (authContext.getUserTenantId()));
                authContext.setNewFlowId(authResponseDTO.getTransactionId()).setPassword(authResponseDTO.getSmsOTP());
            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO authResponseDTO;
                authResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceGenerationResponse
                                        (getAuthenticatorService(authenticator), authContext.getUserId(),
                                                IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()));
                authContext.setNewFlowId(authResponseDTO.getTransactionId()).setPassword(authResponseDTO.getEmailOTP());
            } else if (authenticator.equals(Constants.AUTHENTICATOR_NAME_BASIC_AUTH)) {
                authContext.setNewFlowId(Util.generateUUID());
            } else {
                throw Util.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
        }

        flowIdDO.setFlowIdIdentifier(authContext.getNewFlowdIdIdentifier());
        flowIdDO.setFlowId(authContext.getNewFlowdId());
        CacheBackedFlowIdDAO.getInstance().refreshFlowId(authContext.getFlowIdIdentifier(), flowId, flowIdDO);

        return buildAuthInitializationResponseDTO(authContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAuthenticationResponseDTO authenticateWithClientId
            (String clientId, String authenticator, String password, String userIdentifier, String requestTenantDomain)
            throws AuthenticationException {

        String userTenantDomain = requestTenantDomain == null ?
                PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain() : requestTenantDomain;
        HashMap<String, String> loginAttributeParams;
        loginAttributeParams = getResolvedUsername(userIdentifier, userTenantDomain);

        String resolvedUsername;
        if (StringUtils.isBlank(loginAttributeParams.get(Constants.BASIC_AUTH_PARAM_USERNAME))) {
            resolvedUsername = userIdentifier;
        } else {
            resolvedUsername = loginAttributeParams.get(Constants.BASIC_AUTH_PARAM_USERNAME);
        }

        String fullQualifiedUsername = UserCoreUtil.addTenantDomainToEntry(resolvedUsername, userTenantDomain);
        User user = User.getUserFromUserName(fullQualifiedUsername);

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.BASIC_AUTH_PARAM_USERNAME, fullQualifiedUsername);
        params.put(Constants.BASIC_AUTH_PARAM_CLIENT_ID, clientId);

        AuthRestAuthenticationContext authContext =
                new AuthRestAuthenticationContext.Builder(params).authenticator(authenticator).
                        userTenantId(IdentityTenantUtil.getTenantId(userTenantDomain)).buildForBasicAuth();

        authContext.setUserId(getUserIDFromUserName(user.getUserName(), authContext.getUserTenantId()));
        authContext.setMultiAttributeLoginClaim(loginAttributeParams.get(Constants.LOGGED_USER_CLAIM));

        AuthenticationServiceDataHolder.setLoggedUserClaim(authContext.getMultiAttributeLoginClaim());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        validateAuthStep(authContext.getAuthenticationSteps(), authenticator);
        authContext.setUser(user);
        executeListeners(authContext, Constants.PRE_AUTHENTICATION);

        switch (authenticator) {
            case Constants.AUTHENTICATOR_NAME_IDENTIFIER_FIRST:
                try {
                    if (getUserStoreManager(authContext.getUserTenantId())
                            .isExistingUserWithID(authContext.getUserId())) {
                        getUserStoreManager(authContext.getUserTenantId());
                        updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                    } else {
                        throw Util.handleClientException(
                                Constants.ErrorMessage.CLIENT_INVALID_USER, userIdentifier);
                    }
                } catch (UserStoreException e) {
                    throw Util.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATE_USER_ERROR,
                            String.format("Error while authenticating the user : %s.", userIdentifier), e);
                }
                break;
            case Constants.AUTHENTICATOR_NAME_BASIC_AUTH:
                if (validateUserCredentials(resolvedUsername, password, authContext.getUserTenantId())) {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                } else {
                    throw Util.handleClientException(
                            Constants.ErrorMessage.CLIENT_INCORRECT_USER_CREDENTIALS, userIdentifier);
                }
                break;
        }
        executeListeners(authContext, Constants.POST_AUTHENTICATION);
        if (authContext.getAuthenticationSteps().size() == authContext.getAuthenticatedSteps().size()) {
            authContext.setAuthFlowCompleted(true);
        }
        CacheBackedFlowIdDAO.getInstance().addFlowIdData(buildFlowIdDO(authContext));
        return buildAuthResponseDTO(authContext);

    }

    @Override
    public SPAuthneticationStepsDTO getAuthenticationStepsFromSP(String clientId) throws AuthenticationException {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.BASIC_AUTH_PARAM_CLIENT_ID, clientId);

        AuthRestAuthenticationContext authContext =
                new AuthRestAuthenticationContext
                        .Builder(params)
                        .serviceProvider(clientId)
                        .buildServiceProviderAuthenticationSteps();

        authContext.setAuthenticationsStepsforSP(authContext.getAuthenticationSteps());

        return buildAuthenticationStepDetailsResponse(authContext);

    }


    /**
     * This method returns the resolved username and logged claim uri when multi-attribute login is enabled.
     *
     * @param username          username passed in the request.
     * @param userTenantDomain  tenant domain of the user.
     * @return HashMap          username and logged user claim of the user.
     * @throws AuthenticationException if any server or client error occurred.
     */
    private HashMap<String, String> getResolvedUsername(String username, String userTenantDomain)
            throws AuthenticationException {
        HashMap<String, String> params = new HashMap<>();

        if (AuthenticationServiceDataHolder.getInstance().getMultiAttributeLogin().isEnabled(userTenantDomain)) {
            ResolvedUserResult resolvedUserResult =
                    AuthenticationServiceDataHolder.getInstance().getMultiAttributeLogin()
                            .resolveUser(username, userTenantDomain);
            if (resolvedUserResult != null && ResolvedUserResult.UserResolvedStatus.SUCCESS.
                    equals(resolvedUserResult.getResolvedStatus())) {
                username = resolvedUserResult.getUser().getUsername();

                params.put(Constants.BASIC_AUTH_PARAM_USERNAME, username);
                params.put(Constants.LOGGED_USER_CLAIM, resolvedUserResult.getResolvedClaim());

            } else if (ResolvedUserResult.UserResolvedStatus.FAIL.equals(resolvedUserResult.getResolvedStatus())) {
                if (resolvedUserResult.getErrorMessage() != null) {
                    throw Util.handleClientException(Constants.ErrorMessage.CLIENT_CUSTOM_AUTHENTICATE_USER_ERROR,
                            resolvedUserResult.getErrorMessage());
                }
            } else {
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_INVALID_USER, username);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Multi-Attribute login is not enabled.");
            }
        }

        return params;
    }

    /**
     * This method checks whether cross tenant access is enabled.
     *
     * @param serviceProvider   Service Provider
     * @param userTenantId      user tenant Id.
     * @param spTenantId        service provider tenant Id.
     * @throws AuthenticationException if any server or client error occurred.
     */
    private void validateCrossTenantAccess(ServiceProvider serviceProvider, int userTenantId, int spTenantId)
            throws AuthenticationException {

        if (serviceProvider.isSaasApp() == false) {
            if (userTenantId != spTenantId) {
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_USER_SP_TENANT_MISMATCH);
            }
        }
    }

    /**
     * This method executes authentication listeners.
     *
     * @param authContext       User object of the user.
     * @throws AuthenticationException if any server or client error occurred.
     */
    @SuppressWarnings("checkstyle:FallThrough")
    private void executeListeners(AuthRestAuthenticationContext authContext, String event)
            throws AuthenticationException {

        switch (event) {
            case Constants.PRE_AUTHENTICATION:
                for (AuthenticationListener listener : AuthenticationListenerServiceImpl.getAuthenticationListeners()) {
                    listener.doPreAuthenticate(authContext);
                }
            case Constants.POST_AUTHENTICATION:
                for (AuthenticationListener listener : AuthenticationListenerServiceImpl.getAuthenticationListeners()) {
                    listener.doPostAuthenticate(authContext);
                }
        }
    }

    /**
     * This method gets the Authenticator Service Instance from OSGi Service.
     *
     * @param authenticatorService Authenticator Service instance.
     * @return service instance
     */
    private Object getAuthenticatorService(String authenticatorService) {

        Object serviceInstance = null;

        if (authenticatorService.equals(Constants.AUTHENTICATOR_NAME_SMSOTP) && smsOtpService == null) {
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(SMSOTPService.class, null);
        } else if (authenticatorService.equals(Constants.AUTHENTICATOR_NAME_EMAILOTP) && emailOTPService == null) {
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(EmailOtpService.class, null);
        }

        return serviceInstance;
    }

    /**
     * This method verifies the user to whom the Flow Id was issued.
     *
     * @param flowIdDO          Flow Id Data Object.
     * @param userId            Provided user Id.
     * @return Boolean          whether the provided user is the user to whom the Flow Id was issued.
     * @throws AuthenticationException if any server or client error occurred.
     */
    private boolean validateUser(FlowIdDO flowIdDO, String userId) throws AuthenticationException {

        if (flowIdDO.getUserId().equals(userId)) {
            return true;
        } else {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_USERID_FLOWID_MISMATCH, flowIdDO.getFlowId());
        }
    }

    /**
     * This method validates user credentials.
     *
     * @param username          username of the user.
     * @param password          password of the user.
     * @return Boolean          whether the user is authorized.
     * @throws AuthenticationException if any server or client error occurred.
     */
    private boolean validateUserCredentials(String username, String password, int userTenantId)
            throws AuthenticationException {

        String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(username);
        boolean authorized;
        try {
            authorized = getUserStoreManager(userTenantId).authenticate(tenantAwareUserName, password);
        } catch (UserStoreException e) {
            throw Util.handleClientException(Constants.ErrorMessage.CLIENT_LOCKED_ACCOUNT,
                    String.format("Error while checking the account status for the user : %s.", username), e);
        }
        return authorized;
    }

    /**
     * This method retrieves the user store manager.
     *
     * @return AbstractUserStoreManager user store manager.
     * @throws AuthenticationException         if any server or client error occurred.
     */
    private AbstractUserStoreManager getUserStoreManager(int userTenantId) throws AuthenticationException {

        UserStoreManager userStoreManager;

        try {
            userStoreManager = (AbstractUserStoreManager) AuthenticationServiceDataHolder.getInstance()
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
     * @param username          username of the user.
     * @return String           UUID of the user.
     * @throws AuthenticationException if any server or client error occurred.
     */
    public String getUserIDFromUserName(String username, int userTenantId) throws AuthenticationException {

        String userId;
        String tenantAwareUsername = MultitenantUtils.getTenantAwareUsername(username);

        try {
            userId = getUserStoreManager(userTenantId).getUserIDFromUserName(tenantAwareUsername);

        } catch (UserStoreException e) {
            throw Util.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_USER_ID_ERROR,
                    String.format("Error while retrieving userId for the username : %s.", username), e);
        }

        if (userId == null) {
            throw Util.handleClientException(Constants.ErrorMessage.CLIENT_INVALID_USER, username);
        }

        return userId;

    }

    /**
     * This method retrieves the following authentication step.
     *
     * @param authenticatedSteps  Authentication Steps the user has successfully completed.
     * @param authenticationSteps Authentication Steps defined for the Service Provider.
     * @return Integer            Next authentication step.
     */
    private int fetchNextAuthStep(LinkedHashMap<Integer, String> authenticatedSteps,
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
     * @param authenticationSteps Authentication Steps defined for the Service Provider.
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
     * @param authenticatedSteps  Authentication Steps the user has successfully completed.
     * @param authenticationSteps Authentication Steps defined for the Service Provider.
     * @param authenticator       Name of the Authenticator.
     * @throws AuthenticationException if any server or client error occurred.
     */
    private void validateAuthStep(LinkedHashMap<Integer, String> authenticatedSteps,
                                  LinkedHashMap<Integer, List<String>> authenticationSteps, String authenticator)
            throws AuthenticationException {

        int nextAuthStep = fetchNextAuthStep(authenticatedSteps, authenticationSteps);
        if (nextAuthStep == -1) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_AUTHSTEP_OUT_OF_BOUNDS, authenticator);
        }
        List<String> authenticators = authenticationSteps.get(nextAuthStep);
        if (!authenticators.contains(authenticator)) {
            throw Util.handleClientException(Constants.ErrorMessage.CLIENT_INVALID_AUTHENTICATOR, authenticator);
        }
    }

    /**
     * This method validates the executed Authentication Step.
     *
     * @param authenticationSteps Authentication Steps defined for the Service Provider.
     * @param authenticator       Name of the Authenticator.
     * @throws AuthenticationException   if any server or client error occurred.
     */
    private void validateAuthStep(LinkedHashMap<Integer, List<String>> authenticationSteps, String authenticator)
            throws AuthenticationException {

        int nextAuthStep = fetchNextAuthStep(authenticationSteps);
        if (nextAuthStep == -1) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_AUTHSTEP_OUT_OF_BOUNDS, authenticator);
        }
        List<String> authenticators = authenticationSteps.get(nextAuthStep);
        if (!authenticators.contains(authenticator)) {
            throw Util.handleClientException(
                    Constants.ErrorMessage.CLIENT_INVALID_AUTHENTICATOR, authenticator);
        }
    }

    /**
     * This method builds Flow Id Data Object.
     *
     * @param authContext Authentication context.
     * @return FlowIdDO   Flow Id Data Object.
     */
    private FlowIdDO buildFlowIdDO(AuthRestAuthenticationContext authContext) {

        FlowIdDO flowIdDO = new FlowIdDO();

        flowIdDO.setUserId(authContext.getUserId());
        flowIdDO.setFullQualifiedUserName(User.getUserFromUserName
                (authContext.getUsername()).toFullQualifiedUsername());
        flowIdDO.setFlowIdIdentifier(authContext.getNewFlowdIdIdentifier());
        flowIdDO.setFlowId(authContext.getNewFlowdId());
        flowIdDO.setFlowIdState(authContext.getFlowIdState());
        flowIdDO.setGeneratedTime(System.currentTimeMillis());
        flowIdDO.setExpiryTime(flowIdDO.getGeneratedTime() + authContext.getFlowIdValidityPeriod());
        flowIdDO.setAuthFlowCompleted(authContext.isAuthFlowCompleted());
        flowIdDO.setServiceProviderAppId(authContext.getServiceProvider().getApplicationID());
        flowIdDO.setAuthenticatedSteps(authContext.getAuthenticatedSteps());
        flowIdDO.setSpTenantId(authContext.getSpTenantId());
        flowIdDO.setUserTenantId(authContext.getUserTenantId());

        return flowIdDO;
    }

    /**
     * This method builds Basic Authentication Response DTO.
     *
     * @param authContext                      Authentication context.
     * @return UserAuthenticationResponseDTO   Basic Authentication Response Data Transfer Object.
     */
    private UserAuthenticationResponseDTO buildAuthResponseDTO(AuthRestAuthenticationContext authContext) {

        authContext.setAuthenticationsStepsforSP(authContext.getAuthenticationSteps());

        UserAuthenticationResponseDTO responseDTO = new UserAuthenticationResponseDTO();
        responseDTO.setFlowId(authContext.getNewFlowdId());
        responseDTO.setAuthenticationSteps(authContext.getAuthenticationSteps());
        responseDTO.setAuthenticatedSteps(authContext.getAuthenticatedSteps());
        responseDTO.setAuthenticationStepDetails(authContext.getAuthenticationsStepsforSP());
        responseDTO.setAuthFlowCompleted(authContext.isAuthFlowCompleted());
        responseDTO.setNextStep(fetchNextAuthStep
                (authContext.getAuthenticatedSteps(), authContext.getAuthenticationSteps()));

        return responseDTO;
    }


    /**
     * This method builds Authentication Initialization Response DTO.
     *
     * @param authContext                                 Authentication context.
     * @return AuthenticationInitializationResponseDTO   Authentication Initialization Response Data Transfer Object.
     */
    private AuthenticationInitializationResponseDTO
    buildAuthInitializationResponseDTO(AuthRestAuthenticationContext authContext) {

        AuthenticationInitializationResponseDTO responseDTO = new AuthenticationInitializationResponseDTO();
        responseDTO.setFlowId(authContext.getNewFlowdId());
        responseDTO.setAuthenticator(authContext.getAuthenticator());

        return responseDTO;
    }


    /**
     * This method builds Authentication Validation Response.
     *
     * @param authContext                       Authentication Context.
     * @param failureReasonDTO                  Failure Reason Data Transfer Object.
     * @return UserAuthenticationResponseDTO    Authentication Validation Response.
     */
    private UserAuthenticationResponseDTO
    buildAuthValidationResponse(AuthRestAuthenticationContext authContext, FlowIdDO flowIdDO, Object failureReasonDTO) {

        UserAuthenticationResponseDTO userAuthenticationResponse = new UserAuthenticationResponseDTO();
        AuthenticationValidationFailureReasonDTO failureReason = null;

        if (authContext.isValidPassword()) {
            updateAuthenticatedSteps(authContext.getAuthenticatedSteps(),
                    authContext.getAuthenticatedSteps().size() + 1, authContext.getAuthenticator());

            if (authContext.getAuthenticationSteps().size() == authContext.getAuthenticatedSteps().size()) {
                authContext.setAuthFlowCompleted(true);
                flowIdDO.setAuthFlowCompleted(true);
            }
            flowIdDO.setFlowIdIdentifier(authContext.getNewFlowdIdIdentifier());
            flowIdDO.setFlowId(authContext.getNewFlowdId());
        }

        authContext.setAuthenticationsStepsforSP(authContext.getAuthenticationSteps());

        userAuthenticationResponse.setValid(authContext.isValidPassword())
                .setUserId(authContext.getUserId())
                .setFlowId(authContext.getNewFlowdId()).setAuthFlowCompleted(authContext.isAuthFlowCompleted())
                .setAuthenticatedSteps(authContext.getAuthenticatedSteps())
                .setAuthenticationStepDetails
                        (authContext.getAuthenticationsStepsforSP())
                .setNextStep(fetchNextAuthStep(authContext.getAuthenticatedSteps(),
                        authContext.getAuthenticationSteps()));

        if (failureReasonDTO != null) {
            if (failureReasonDTO instanceof FailureReasonDTO) {

                FailureReasonDTO failureReasonDO = (FailureReasonDTO) failureReasonDTO;
                failureReason = new AuthenticationValidationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());

            } else if (failureReasonDTO instanceof
                    org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) {

                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) failureReasonDTO;
                failureReason = new AuthenticationValidationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());

            }
            userAuthenticationResponse.setFailureReason(failureReason);
        }
        return userAuthenticationResponse;
    }


    private SPAuthneticationStepsDTO buildAuthenticationStepDetailsResponse
            (AuthRestAuthenticationContext authContext) {

        SPAuthneticationStepsDTO responseDTO = new SPAuthneticationStepsDTO();
        responseDTO.setAuthenticationStepContextList(authContext.getAuthenticationsStepsforSP());

        return responseDTO;
    }


    /**
     * This method checks whether the provided Flow Id is valid.
     *
     * @param flowIdDO          Flow Id Data Object.
     * @return boolean          whether the Flow Id is ACTIVE or not.
     * @throws AuthenticationException if any server or client error occurred.
     */
    public boolean isValidFlowId(FlowIdDO flowIdDO) throws AuthenticationException {

        boolean isValidFlowId = false;
        int flowIdTimestampSkew = AuthenticationServiceDataHolder.getConfigs().getTimestampSkew();

        if (System.currentTimeMillis() > (flowIdDO.getExpiryTime() + flowIdTimestampSkew)) {
            CacheBackedFlowIdDAO.getInstance().updateFlowIdState(flowIdDO.getFlowId(),
                    Constants.FLOW_ID_STATE_EXPIRED);
            flowIdDO.setFlowIdState(Constants.FLOW_ID_STATE_EXPIRED);
        }

        switch (flowIdDO.getFlowIdState()) {
            case Constants.FLOW_ID_STATE_ACTIVE:
                isValidFlowId = true;
                break;
            case Constants.FLOW_ID_STATE_INACTIVE:
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_INACTIVE_FLOW_ID, flowIdDO.getFlowId());
            case Constants.FLOW_ID_STATE_EXPIRED:
                throw Util.handleClientException(
                        Constants.ErrorMessage.CLIENT_EXPIRED_FLOW_ID, flowIdDO.getFlowId());
        }

        return isValidFlowId;
    }

    /**
     * This method adds successfully authenticated steps into the authenticatedSteps Object.
     *
     * @param authenticatedSteps Existing authenticatedSteps Object.
     * @param stepNo             Successfully authenticated Authentication Step Number.
     * @param authenticator      Successfully authenticated Authenticator.
     */
    private void updateAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps, Integer stepNo,
                                          String authenticator) {
        authenticatedSteps.put(stepNo, authenticator);
    }
}
