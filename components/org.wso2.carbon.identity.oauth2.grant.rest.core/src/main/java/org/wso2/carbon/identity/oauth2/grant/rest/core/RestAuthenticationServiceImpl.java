/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.grant.rest.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorStatus;
import org.wso2.carbon.identity.application.authentication.framework.config.model.SequenceConfig;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.multi.attribute.login.mgt.ResolvedUserResult;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.context.RestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.CacheBackedFlowIdDAO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.FlowIdDAOImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.FlowIdDO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthStepConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticatedAuthenticatorDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticationInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticationStepsResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticatorConfigDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationServerException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.handler.OTPHandlerService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.handler.OTPHandlerServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.core.internal.AuthenticationServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.rest.core.listener.AuthenticationListener;
import org.wso2.carbon.identity.oauth2.grant.rest.core.util.RestAuthUtil;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO;
import org.wso2.carbon.identity.smsotp.common.dto.GenerationResponseDTO;
import org.wso2.carbon.identity.smsotp.common.dto.ValidationResponseDTO;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.common.AuthenticationResult;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the RestAuthenticationService interface.
 */
public class RestAuthenticationServiceImpl implements RestAuthenticationService {

    private static final Log LOG = LogFactory.getLog(RestAuthenticationServiceImpl.class);

    /**
     * This method returns the Steps for the Authentication flow.
     *
     * @param clientId                          UUID to track the flow
     * @throws AuthenticationException          If any server or client error occurred.
     * return AuthenticationStepsResponseDTO    An object of AuthenticationStepsResponseDTO.
     */
    @Override
    public AuthenticationStepsResponseDTO getAuthenticationStepsFromSP(String clientId)
            throws AuthenticationException {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.BASIC_AUTH_PARAM_CLIENT_ID, clientId);

        RestAuthenticationContext authContext =
                new RestAuthenticationContext
                        .Builder(params)
                        .serviceProvider(clientId)
                        .buildServiceProviderAuthenticationSteps();

        return buildAuthenticationStepsForSP(authContext);

    }

    /**
     * This method process the authentication response from the client.
     *
     * @param flowId                            UUID to track the flow.
     * @param authenticator                     Authenticator Name.
     * @param password                          Password to be validated.
     * @return UserAuthenticationResponseDTO    An object of UserAuthenticationResponseDTO.
     * @throws AuthenticationException          If any server or client error occurred.
     */
    @Override
    public UserAuthenticationResponseDTO processAuthStepResponse(String flowId, String authenticator, String password)
            throws AuthenticationException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("authentication flow started with flowId,authenticator name and password");
        }

        OTPHandlerService authenticationStepExecutorService =
                new OTPHandlerServiceImpl();

        UserAuthenticationResponseDTO userAuthenticationResponse = null;
        boolean showFailureReason = AuthenticationServiceDataHolder.getConfigs().isShowFailureReason();

        if (LOG.isDebugEnabled()) {
            LOG.debug("ShowFailureReason enabled.");
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.VALIDATE_PARAM_FLOW_ID, flowId);
        params.put(Constants.VALIDATE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.VALIDATE_PARAM_PASSWORD, password);

        RestAuthenticationContext authContext =
                new RestAuthenticationContext.Builder(params).buildForAuthStepValidate();

        FlowIdDO flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);
        authContext.setUserId(flowIdDO.getUserId());

        User user = User.getUserFromUserName(flowIdDO.getFullQualifiedUserName());
        authContext.setUser(user);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setTenantDomain(user.getTenantDomain());
        authenticatedUser.setUserStoreDomain(user.getUserStoreDomain());
        authenticatedUser.setUserName(user.getUserName());
        authenticatedUser.setAuthenticatedSubjectIdentifier(user.getUserStoreDomain() + "/" + user.getTenantDomain());

        authContext.setMultiAttributeLoginClaim(flowIdDO.getLoggedUserClaim());
        authContext.setAuthenticatedUser(authenticatedUser);

        authContext.setServiceProvider(flowIdDO.getServiceProviderAppId());
        authContext.setAuthenticatedSteps(flowIdDO.getAuthenticatedSteps());
        authContext.setFlowIdIdentifier(flowIdDO.getFlowIdIdentifier());
        authContext.setUserTenantId(flowIdDO.getUserTenantId());
        authContext.setSpTenantId(flowIdDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        executeListeners(authContext, Constants.PRE_AUTHENTICATION);

        if (isValidFlowId(flowIdDO) && validateUser(flowIdDO, authContext.getUserId())) {

            validateAuthStep(authContext, authenticator);
            Object authnFailDto = null;
            String federatedAuthenticatorName = null;

            if (!Constants.AUTHENTICATOR_NAME_BASIC_AUTH.equals(authenticator)) {
                federatedAuthenticatorName = getFederatedIdpChannelName(authenticator, authContext);
            }

            if (StringUtils.isNotEmpty(federatedAuthenticatorName) &&
                    Constants.AUTHENTICATOR_NAME_SMSOTP.equals(federatedAuthenticatorName)) {
                ValidationResponseDTO authResponseDTO;
                authResponseDTO =
                        (ValidationResponseDTO) authenticationStepExecutorService.
                                getAuthenticatorServiceValidationResponse
                                        (getAuthenticatorService(federatedAuthenticatorName), authContext, password);

                authContext.setValidPassword(authResponseDTO.isValid());
                if (authResponseDTO.isValid()) {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(),
                            authContext.getAuthenticatedSteps().size() + 1, authContext.getCurrentAuthenticator());
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString(), authContext);
                } else {
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(), authContext);
                }
                org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO failureReasonDTO =
                        showFailureReason ? authResponseDTO.getFailureReason() : null;

                authnFailDto = failureReasonDTO;

            } else if (StringUtils.isNotEmpty(federatedAuthenticatorName) &&
                    Constants.AUTHENTICATOR_NAME_EMAILOTP.equals(federatedAuthenticatorName)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO authResponseDTO;
                authResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.ValidationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceValidationResponse
                                        (getAuthenticatorService(federatedAuthenticatorName), authContext, password);

                authContext.setValidPassword(authResponseDTO.isValid());
                if (authResponseDTO.isValid()) {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(),
                            authContext.getAuthenticatedSteps().size() + 1, authContext.getCurrentAuthenticator());
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString(), authContext);
                } else {
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(), authContext);
                }
                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDTO =
                        showFailureReason ?
                                authResponseDTO.getFailureReason() : null;
                authnFailDto = failureReasonDTO;
            } else if (Constants.AUTHENTICATOR_NAME_BASIC_AUTH.equals(authenticator)) {
                boolean isValidPwd = validateUserCredentials(password, authContext);
                authContext.setValidPassword(isValidPwd);

                AuthenticationFailureReasonDTO basicAuthenticatorFailureReason = null;
                if (!isValidPwd) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("The given password is not valid.");
                    }
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(), authContext);
                    basicAuthenticatorFailureReason = new AuthenticationFailureReasonDTO
                            (Constants.INVALID_BASIC_AUTHENTICATOR_PASSWORD_ERROR_CODE,
                                    Constants.INVALID_BASIC_AUTHENTICATOR_PASSWORD_ERROR_MESSAGE,
                                    Constants.INVALID_BASIC_AUTHENTICATOR_PASSWORD_ERROR_DESCRIPTION);

                    authnFailDto = showFailureReason ? basicAuthenticatorFailureReason : null;
                } else {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(),
                            authContext.getAuthenticatedSteps().size() + 1, authContext.getCurrentAuthenticator());
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString(), authContext);
                }

            } else {
                throw RestAuthUtil.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
            userAuthenticationResponse = buildAuthValidationResponse(authContext, flowIdDO, authnFailDto);
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
     * This method initialize the authentication flow for the current step.
     *
     * @param flowId                                    UUID to track the flow.
     * @param authenticator                             Authenticator Name.
     * @return AuthenticationInitializationResponseDTO  An object of AuthenticationInitializationResponseDTO.
     * @throws AuthenticationException                  If any server or client error occurred.
     */
    @Override
    public AuthenticationInitializationResponseDTO
    executeAuthStep(String flowId, String authenticator) throws AuthenticationException {

        MultiAttributeLoginService multiAttributeLoginService = AuthenticationServiceDataHolder.
                getInstance().getMultiAttributeLogin();

        OTPHandlerService authenticationStepExecutorService =
                new OTPHandlerServiceImpl();

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.INITIALIZE_PARAM_AUTHENTICATOR, authenticator);
        params.put(Constants.INITIALIZE_PARAM_FLOW_ID, flowId);

        RestAuthenticationContext authContext =
                new RestAuthenticationContext.Builder(params).buildForAuthStepInitialize();

        FlowIdDO flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);

        authContext.setUserId(flowIdDO.getUserId());
        authContext.setServiceProvider(flowIdDO.getServiceProviderAppId());
        authContext.setFlowIdIdentifier(flowIdDO.getFlowIdIdentifier());
        authContext.setUserTenantId(flowIdDO.getUserTenantId());
        authContext.setSpTenantId(flowIdDO.getSpTenantId());

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        if (isValidFlowId(flowIdDO) && validateUser(flowIdDO, authContext.getUserId())) {

            authContext.setAuthenticatedSteps(flowIdDO.getAuthenticatedSteps());
            validateAuthStep(authContext, authenticator);

            String federatedAuthenticatorName = null;
            if (!Constants.AUTHENTICATOR_NAME_BASIC_AUTH.equals(authenticator)) {
                federatedAuthenticatorName = getFederatedIdpChannelName(authenticator, authContext);
            }

            if (StringUtils.isNotEmpty(federatedAuthenticatorName) &&
                    Constants.AUTHENTICATOR_NAME_SMSOTP.equals(federatedAuthenticatorName)) {
                GenerationResponseDTO authResponseDTO = (GenerationResponseDTO) authenticationStepExecutorService.
                        getAuthenticatorServiceGenerationResponse(getAuthenticatorService(federatedAuthenticatorName),
                                authContext.getUserId(), IdentityTenantUtil.getTenantDomain
                                        (authContext.getUserTenantId()));
                authContext.setNewFlowId(authResponseDTO.getTransactionId()).setPassword(authResponseDTO.getSmsOTP());
                authContext.setUserChannelIdentifierClaim(getUserVerificationChannel
                        (authContext.getUserId(), authContext.getUserTenantId(), Constants.MOBILE_LOCAL_CLAIM_URI));
            } else if (StringUtils.isNotEmpty(federatedAuthenticatorName) &&
                    Constants.AUTHENTICATOR_NAME_EMAILOTP.equals(federatedAuthenticatorName)) {
                //below the package name is used since there's a class level object conflict.
                org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO authResponseDTO;
                authResponseDTO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.GenerationResponseDTO)
                                authenticationStepExecutorService.getAuthenticatorServiceGenerationResponse
                                        (getAuthenticatorService(federatedAuthenticatorName), authContext.getUserId(),
                                                IdentityTenantUtil.getTenantDomain(authContext.getUserTenantId()));
                authContext.setNewFlowId(authResponseDTO.getTransactionId()).setPassword(authResponseDTO.getEmailOTP());
                authContext.setUserChannelIdentifierClaim(getUserVerificationChannel
                        (authContext.getUserId(), authContext.getUserTenantId(), Constants.EMAIL_LOCAL_CLAIM_URI));
            } else if (Constants.AUTHENTICATOR_NAME_BASIC_AUTH.equals(authenticator)) {
                authContext.setNewFlowId(RestAuthUtil.generateUUID());
            } else {
                LOG.error("The given authenticator is not configured in current step.");
                throw RestAuthUtil.handleClientException(Constants.ErrorMessage.CLIENT_AUTHENTICATOR_NOT_SUPPORTED,
                        authenticator);
            }
        }

        flowIdDO.setFlowIdIdentifier(authContext.getNewFlowdIdIdentifier());
        flowIdDO.setFlowId(authContext.getNewFlowdId());
        CacheBackedFlowIdDAO.getInstance().refreshFlowId(authContext.getFlowIdIdentifier(), flowId, flowIdDO);

        return buildAuthInitializationResponseDTO(authContext);
    }

    /**
     * This method initialize the authentication flow with BasicAuth or Identifier First.
     *
     * @param clientId                          The clientId of service provider.
     * @param authenticator                     The name of the authenticator.
     * @param password                          Password to be validated.
     * @param userIdentifier                    User identifier attribute used during the authentication.
     * @param requestTenantDomain               Tenant domain passed in the API request.
     * @return UserAuthenticationResponseDTO    An object of UserAuthenticationResponseDTO.
     * @throws AuthenticationException          If any server or client error occurred.
     */
    @Override
    public UserAuthenticationResponseDTO initializeAuthFlow (String clientId, String authenticator, String password,
                                                             String userIdentifier, String requestTenantDomain)
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

        if (LOG.isDebugEnabled()) {
            LOG.debug("Full qualified username : " + fullQualifiedUsername);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.BASIC_AUTH_PARAM_USERNAME, fullQualifiedUsername);
        params.put(Constants.BASIC_AUTH_PARAM_CLIENT_ID, clientId);

        RestAuthenticationContext authContext =
                new RestAuthenticationContext.Builder(params).authenticator(authenticator).
                        userTenantId(IdentityTenantUtil.getTenantId(userTenantDomain)).buildForAuthFlowInitialize();

        String usernameWithUserStoreDomain = user.getUserStoreDomain() + "/" + user.getUserName();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolved username from user store: " + usernameWithUserStoreDomain);
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setTenantDomain(user.getTenantDomain());
        authenticatedUser.setUserStoreDomain(user.getUserStoreDomain());
        authenticatedUser.setUserName(user.getUserName());
        authenticatedUser.setAuthenticatedSubjectIdentifier(usernameWithUserStoreDomain);

        authContext.setAuthenticatedUser(authenticatedUser);

        authContext.setUserId(getUserIDFromUserName(usernameWithUserStoreDomain,
                authContext.getUserTenantId(), authContext));

        if (StringUtils.isEmpty(loginAttributeParams.get(Constants.LOGGED_USER_CLAIM))) {
            authContext.setMultiAttributeLoginClaim(Constants.USERNAME_LOCAL_CLAIM_URI);
        } else {
            authContext.setMultiAttributeLoginClaim(loginAttributeParams.get(Constants.LOGGED_USER_CLAIM));
        }

        validateCrossTenantAccess(authContext.getServiceProvider(), authContext.getUserTenantId(),
                authContext.getSpTenantId());

        validateAuthStep(authContext, authenticator);
        authContext.setUser(user);
        executeListeners(authContext, Constants.PRE_AUTHENTICATION);

        switch (authenticator) {
            case Constants.AUTHENTICATOR_NAME_IDENTIFIER_FIRST:
                try {
                    if (getUserStoreManager(authContext.getUserTenantId())
                            .isExistingUserWithID(authContext.getUserId())) {
                        getUserStoreManager(authContext.getUserTenantId());
                        updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                        authContext.setValidPassword(true);
                        executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString(),
                                authContext);
                    } else {
                        executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(),
                                authContext);
                        throw RestAuthUtil.handleClientException(
                                Constants.ErrorMessage.CLIENT_INVALID_USER, userIdentifier);
                    }
                } catch (UserStoreException e) {
                    throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATE_USER_ERROR,
                            String.format("Error while authenticating the user : %s.", userIdentifier), e);
                }
                break;
            case Constants.AUTHENTICATOR_NAME_BASIC_AUTH:
                if (validateUserCredentials(password, authContext)) {
                    updateAuthenticatedSteps(authContext.getAuthenticatedSteps(), 1, authenticator);
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString(), authContext);
                } else {
                    executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(), authContext);
                    throw RestAuthUtil.handleClientException(
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

    /**
     * This method returns the resolved username and logged claim uri when multi-attribute login is enabled.
     *
     * @param username                 Username passed in the request.
     * @param userTenantDomain         Tenant domain of the user.
     * @return HashMap                 Username and logged user claim of the user.
     * @throws AuthenticationException If any server or client error occurred.
     */
    private HashMap<String, String> getResolvedUsername(String username, String userTenantDomain)
            throws AuthenticationException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Revolving user identifier to username");
        }

        HashMap<String, String> params = new HashMap<>();
        if (AuthenticationServiceDataHolder.getInstance().getMultiAttributeLogin().isEnabled(userTenantDomain)) {
            ResolvedUserResult resolvedUserResult =
                    AuthenticationServiceDataHolder.getInstance().getMultiAttributeLogin()
                            .resolveUser(username, userTenantDomain);
            if (resolvedUserResult != null && ResolvedUserResult.UserResolvedStatus.SUCCESS.
                    equals(resolvedUserResult.getResolvedStatus())) {
                username = resolvedUserResult.getUser().getUsername();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Username resolved successfully : " + username);
                }
                params.put(Constants.BASIC_AUTH_PARAM_USERNAME, username);
                params.put(Constants.LOGGED_USER_CLAIM, resolvedUserResult.getResolvedClaim());

            } else if (ResolvedUserResult.UserResolvedStatus.FAIL.equals(resolvedUserResult.getResolvedStatus())) {
                if (resolvedUserResult.getErrorMessage() != null) {
                    throw RestAuthUtil.handleClientException
                            (Constants.ErrorMessage.CLIENT_CUSTOM_AUTHENTICATE_USER_ERROR,
                                    resolvedUserResult.getErrorMessage());
                }
            } else {
                throw RestAuthUtil.handleClientException(
                        Constants.ErrorMessage.CLIENT_INVALID_USER, username);
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Multi-Attribute login is not enabled.");
            }
        }

        return params;
    }

    /**
     * This method checks whether cross tenant access is enabled.
     *
     * @param serviceProvider           Service Provider.
     * @param userTenantId              User tenant Id.
     * @param spTenantId                Service provider tenant ID.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    private void validateCrossTenantAccess(ServiceProvider serviceProvider, int userTenantId, int spTenantId)
            throws AuthenticationException {

        if (!serviceProvider.isSaasApp() && userTenantId != spTenantId) {
            throw RestAuthUtil.handleClientException(
                    Constants.ErrorMessage.CLIENT_USER_SP_TENANT_MISMATCH);
        }
    }

    /**
     * This method executes authentication listeners.
     *
     * @param authContext              User object of the user.
     * @param event                    Event name.
     * @throws AuthenticationException If any server or client error occurred.
     */
    private void executeListeners(RestAuthenticationContext authContext, String event)
            throws AuthenticationException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(event + " listener triggered.");
        }

        switch (event) {
            case Constants.PRE_AUTHENTICATION:
                for (AuthenticationListener listener : AuthenticationListenerServiceImpl.getAuthenticationListeners()) {
                    listener.doPreAuthenticate(authContext);
                }
                break;
            case Constants.POST_AUTHENTICATION:
                for (AuthenticationListener listener : AuthenticationListenerServiceImpl.getAuthenticationListeners()) {
                    listener.doPostAuthenticate(authContext);
                }
                break;
        }
    }

    /**
     * This method gets the Authenticator Service Instance from OSGi Service.
     *
     * @param authenticatorService      Authenticator Service instance.
     * @return String                   Service instance name.
     * @throws AuthenticationException  Returns as AuthenticationException.
     */
    private String getAuthenticatorService(String authenticatorService) throws AuthenticationException {

        Object serviceInstance;

        if (Constants.AUTHENTICATOR_NAME_SMSOTP.equals(authenticatorService)) {
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(SMSOTPService.class, null);
        } else if (Constants.AUTHENTICATOR_NAME_EMAILOTP.equals(authenticatorService)) {
            serviceInstance = PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(EmailOtpService.class, null);
        } else {
            LOG.error("Authenticator Service is not available : " + authenticatorService);
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATOR_SERVICE_ERROR,
                    String.format("Authenticator Service Object is neither SMSOTP nor EmailOTP."));
        }

        return serviceInstance.getClass().getName();
    }

    /**
     * This method verifies the user to whom the FlowId was issued.
     *
     * @param flowIdDO                  Flow Id Data Object.
     * @param userId                    Provided userId.
     * @return Boolean                  Whether the provided user is the user to whom the FlowId was issued.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    private boolean validateUser(FlowIdDO flowIdDO, String userId) throws AuthenticationException {

        if (flowIdDO.getUserId().equals(userId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Provided flow is valid with the authenticated user.");
            }
            return true;
        } else {
            throw RestAuthUtil.handleClientException(
                    Constants.ErrorMessage.CLIENT_USERID_FLOWID_MISMATCH, flowIdDO.getFlowId());
        }
    }

    /**
     * This method validates user credentials.
     *
     * @param password                  Password of the user.
     * @param restContext               RestAuthenticationContext from connector.
     * @return Boolean                  Whether the user is authorized.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    private boolean validateUserCredentials(String password, RestAuthenticationContext restContext)
            throws AuthenticationException {

        try {

            AuthenticationResult authenticationResult =
                    getUserStoreManager(restContext.getUserTenantId()).authenticateWithID(restContext.getUserId(),
                            password);

            if (Constants.SUCCESS_AUTHENTICATION_STATUS.equals(authenticationResult.getAuthenticationStatus()
                    .toString())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(restContext.getUserName() + " : user successfully authenticated.");
                }
                return true;
            } else {
                return false;
            }
        } catch (UserStoreException e) {
            LOG.error("User authentication failed from user store.");
            String message = e.getCause().getLocalizedMessage();
            if (message.contains("disabled")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Account is disabled for user : " + restContext.getAuthenticatedUser().getUserName());
                }
                throw RestAuthUtil.handleClientException(Constants.ErrorMessage.CLIENT_DISABLED_ACCOUNT,
                        String.format("Error while checking the account status for the user : %s.",
                                restContext.getAuthenticatedUser().getUserName()),
                        e);
            } else if (message.contains("locked")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Account is locked for user : " + restContext.getAuthenticatedUser().getUserName());
                }
                throw RestAuthUtil.handleClientException(Constants.ErrorMessage.CLIENT_LOCKED_ACCOUNT,
                        String.format("Error while checking the account status for the user : %s.",
                                restContext.getAuthenticatedUser().getUserName()));
            }
            return false;
        }
    }

    /**
     * This method retrieves the user store manager.
     *
     * @param userTenantId                  Tenant id of the authenticated user.
     * @return AbstractUserStoreManager     An object on AbstractUserStoreManager.
     * @throws AuthenticationException      If any server or client error occurred.
     */
    private AbstractUserStoreManager getUserStoreManager(int userTenantId) throws AuthenticationException {

        UserStoreManager userStoreManager;

        try {
            userStoreManager = (AbstractUserStoreManager) AuthenticationServiceDataHolder.getInstance()
                    .getRealmService().getTenantUserRealm(userTenantId).getUserStoreManager();
        } catch (UserStoreException e) {
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_USER_STORE_MANAGER_ERROR,
                    "Error while retrieving user store manager.", e);
        }

        return (AbstractUserStoreManager) userStoreManager;
    }

    /**
     * This method retrieves userId using Username.
     *
     * @param username                  Username of the user.
     * @param userTenantId              Tenant id of the authenticated user.
     * @param authContext               An object of RestAuthenticationContext.
     * @return String                   UUID of the user.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    private String getUserIDFromUserName(String username, int userTenantId, RestAuthenticationContext authContext)
            throws AuthenticationException {

        String userId;
        String tenantAwareUsername = MultitenantUtils.getTenantAwareUsername(username);

        try {
            userId = getUserStoreManager(userTenantId).getUserIDFromUserName(tenantAwareUsername);

        } catch (UserStoreException e) {
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_USER_ID_ERROR,
                    String.format("Error while retrieving userId for the username : %s.", username), e);
        }

        if (userId == null) {
            executeEvent(IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString(),
                    authContext);
            throw RestAuthUtil.handleClientException(Constants.ErrorMessage.CLIENT_INVALID_USER, username);
        }

        return userId;

    }

    /**
     * This method retrieves the following authentication step.
     *
     * @param authenticatedSteps    Authentication Steps the user has successfully completed.
     * @param authenticationSteps   Authentication Steps defined for the Service Provider.
     * @return Integer              Next authentication step.
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
     * This method validates the executed Authentication Step.
     *
     * @param authContext               Current AuthenticationContext on REST Flow
     * @param authenticator             Name of the Authenticator.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    private void validateAuthStep(RestAuthenticationContext authContext, String authenticator)
            throws AuthenticationException {

        int nextAuthStep = fetchNextAuthStep(authContext.getAuthenticatedSteps(),
                authContext.getAuthenticationSteps());

        authContext.setNextAuthenticationStepId(nextAuthStep);
        if (nextAuthStep == -1) {
            LOG.error("Auth Steps out of bound.");
            throw RestAuthUtil.handleClientException(
                    Constants.ErrorMessage.CLIENT_AUTHSTEP_OUT_OF_BOUNDS, authenticator);
        }
        List<String> authenticators = authContext.getAuthenticationSteps().get(nextAuthStep);
        if (!authenticators.contains(authenticator)) {
            LOG.error("Invalid authenticator : " + authenticator);
            throw RestAuthUtil.handleClientException
                    (Constants.ErrorMessage.CLIENT_INVALID_AUTHENTICATOR, authenticator);
        }
    }

    /**
     * This method builds FlowId Data Object.
     *
     * @param authContext   Authentication context.
     * @return FlowIdDO     Flow Id Data Object.
     */
    private FlowIdDO buildFlowIdDO(RestAuthenticationContext authContext) {

        FlowIdDO flowIdDO = new FlowIdDO();

        flowIdDO.setUserId(authContext.getUserId());
        flowIdDO.setFullQualifiedUserName(User.getUserFromUserName
                (authContext.getUserName()).toFullQualifiedUsername());
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
        flowIdDO.setLoggedUserClaim(authContext.getMultiAttributeLoginClaim());

        return flowIdDO;
    }

    /**
     * This method builds Basic Authentication Response DTO.
     *
     * @param authContext                      Authentication context.
     * @return UserAuthenticationResponseDTO   Basic Authentication Response Data Transfer Object.
     */
    private UserAuthenticationResponseDTO buildAuthResponseDTO(RestAuthenticationContext authContext) {

        UserAuthenticationResponseDTO responseDTO = new UserAuthenticationResponseDTO();
        responseDTO.setFlowId(authContext.getNewFlowdId());
        responseDTO.setAuthenticationSteps(getConfiguredAuthenticationStepsForSP(authContext.getAuthenticationSteps()));
        responseDTO.setAuthenticatedSteps(getConfiguredAuthenticatedStepsForSP(authContext.getAuthenticatedSteps()));
        responseDTO.setAuthFlowCompleted(authContext.isAuthFlowCompleted());
        responseDTO.setNextStep(fetchNextAuthStep
                (authContext.getAuthenticatedSteps(), authContext.getAuthenticationSteps()));
        responseDTO.setValid(true);

        return responseDTO;
    }


    /**
     * This method builds Authentication Initialization Response DTO.
     *
     * @param authContext                                 Authentication context.
     * @return AuthenticationInitializationResponseDTO    Authentication Initialization Response Data Transfer Object.
     */
    private AuthenticationInitializationResponseDTO
    buildAuthInitializationResponseDTO(RestAuthenticationContext authContext) {

        AuthenticationInitializationResponseDTO responseDTO = new AuthenticationInitializationResponseDTO();
        responseDTO.setFlowId(authContext.getNewFlowdId());
        responseDTO.setAuthenticator(authContext.getCurrentAuthenticator());
        responseDTO.setUserChannelIdentifierClaim(authContext.getUserChannelIdentifierClaim());
        return responseDTO;
    }

    /**
     * This method builds Authentication Validation Response.
     *
     * @param authContext                       Authentication Context.
     * @param failureReasonDTO                  Failure Reason Data Transfer Object.
     * @return UserAuthenticationResponseDTO    Authentication Validation Response.
     */
    private UserAuthenticationResponseDTO buildAuthValidationResponse(RestAuthenticationContext authContext,
                                                                      FlowIdDO flowIdDO, Object failureReasonDTO)
            throws AuthenticationClientException {

        UserAuthenticationResponseDTO userAuthenticationResponse = new UserAuthenticationResponseDTO();
        AuthenticationFailureReasonDTO failureReason = null;

        if (authContext.isValidPassword()) {

            if (authContext.getAuthenticationSteps().size() == authContext.getAuthenticatedSteps().size()) {
                authContext.setAuthFlowCompleted(true);
                flowIdDO.setAuthFlowCompleted(true);
            }
            flowIdDO.setFlowIdIdentifier(authContext.getNewFlowdIdIdentifier());
            flowIdDO.setFlowId(authContext.getNewFlowdId());
        }

        userAuthenticationResponse.setValid(authContext.isValidPassword())
                .setFlowId(authContext.getNewFlowdId()).setAuthFlowCompleted(authContext.isAuthFlowCompleted())
                .setAuthenticatedSteps(getConfiguredAuthenticatedStepsForSP(authContext.getAuthenticatedSteps()))
                .setAuthenticationSteps(getConfiguredAuthenticationStepsForSP(authContext.getAuthenticationSteps()))
                .setNextStep(fetchNextAuthStep(authContext.getAuthenticatedSteps(),
                        authContext.getAuthenticationSteps()));

        if (failureReasonDTO != null) {
            if (failureReasonDTO instanceof FailureReasonDTO) {

                FailureReasonDTO failureReasonDO = (FailureReasonDTO) failureReasonDTO;
                failureReason = new AuthenticationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());

            } else if (failureReasonDTO instanceof
                    org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) {

                org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO failureReasonDO =
                        (org.wso2.carbon.extension.identity.emailotp.common.dto.FailureReasonDTO) failureReasonDTO;
                failureReason = new AuthenticationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());

            } else if (failureReasonDTO instanceof
                    org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO) {

                org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO failureReasonDO =
                        (org.wso2.carbon.identity.smsotp.common.dto.FailureReasonDTO) failureReasonDTO;
                failureReason = new AuthenticationFailureReasonDTO(failureReasonDO.getCode(),
                        failureReasonDO.getMessage(), failureReasonDO.getDescription());

            } else if (failureReasonDTO instanceof AuthenticationFailureReasonDTO) {
                failureReason = new AuthenticationFailureReasonDTO(
                        ((AuthenticationFailureReasonDTO) failureReasonDTO).getCode(),
                        ((AuthenticationFailureReasonDTO) failureReasonDTO).getMessage(),
                        ((AuthenticationFailureReasonDTO) failureReasonDTO).getDescription()
                );
            }
            userAuthenticationResponse.setFailureReason(failureReason);
        }
        return userAuthenticationResponse;
    }

    /**
     * This method is used to return step vice configured authenticator details.
     *
     * @param  authContext                      Rest AuthenticationContext of the flow.
     * @return AuthenticationStepsResponseDTO   Authentication steps details response.
     */
    private AuthenticationStepsResponseDTO buildAuthenticationStepsForSP(RestAuthenticationContext authContext) {

        AuthenticationStepsResponseDTO responseDTO = new AuthenticationStepsResponseDTO();
        responseDTO.setAuthenticationSteps(getConfiguredAuthenticationStepsForSP(authContext.getAuthenticationSteps()));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authentication steps returned from SP configurations.");
        }
        return responseDTO;
    }

    /**
     * This method is used to return step vice configured authenticator details for authentication steps.
     *
     * @param authenticationSteps   Authentication steps.
     * @return List                 Authentication steps details list.
     */
    private static List<AuthStepConfigsDTO> getConfiguredAuthenticationStepsForSP(
            LinkedHashMap<Integer, List<String>> authenticationSteps) {

        AuthenticatorConfigDTO authenticatorConfigDTO;
        AuthStepConfigsDTO authStepConfigsDTO;
        List<AuthStepConfigsDTO> authenticationStepDetails = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> entry : authenticationSteps.entrySet()) {

            int authenticatorStep = entry.getKey();
            List<String> authenticatorName = entry.getValue();

            ArrayList<AuthenticatorConfigDTO> authenticatorDetailsDTOList = new ArrayList<>();

            for (String name : authenticatorName) {
                authenticatorConfigDTO = new AuthenticatorConfigDTO();
                authenticatorConfigDTO.setAuthenticatorName(name);
                authenticatorDetailsDTOList.add(authenticatorConfigDTO);
            }

            authStepConfigsDTO = new AuthStepConfigsDTO();
            authStepConfigsDTO.setStepNo(authenticatorStep);
            authStepConfigsDTO.setAuthenticatorDetails(authenticatorDetailsDTOList);
            authenticationStepDetails.add(authStepConfigsDTO);
        }
        return authenticationStepDetails;
    }

    /**
     * This method is used to return step vice configured authenticator details for authenticated steps.
     *
     * @param authenticatedSteps     Authenticated steps.
     * @return List                  Authenticated steps details list.
     */
    private static List<AuthenticatedAuthenticatorDTO> getConfiguredAuthenticatedStepsForSP(
            LinkedHashMap<Integer, String> authenticatedSteps) {

        AuthenticatedAuthenticatorDTO authenticatedAuthenticatorDTO;
        List<AuthenticatedAuthenticatorDTO> authenticatedStepDetails = new ArrayList<>();

        for (Map.Entry<Integer, String> entry :  authenticatedSteps.entrySet()) {

            authenticatedAuthenticatorDTO = new AuthenticatedAuthenticatorDTO();
            authenticatedAuthenticatorDTO.setStepNo(entry.getKey());
            authenticatedAuthenticatorDTO.setAuthenticatorName(entry.getValue());
            authenticatedStepDetails.add(authenticatedAuthenticatorDTO);

        }
        return authenticatedStepDetails;
    }

    /**
     * This method checks whether the provided FlowId is valid.
     *
     * @param flowIdDO                  Flow Id Data Object.
     * @return boolean                  Whether the FlowId is ACTIVE or not.
     * @throws AuthenticationException  If any server or client error occurred.
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
                throw RestAuthUtil.handleClientException(
                        Constants.ErrorMessage.CLIENT_INACTIVE_FLOW_ID, flowIdDO.getFlowId());
            case Constants.FLOW_ID_STATE_EXPIRED:
                throw RestAuthUtil.handleClientException(
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
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Authentication step success and updated for authenticator : %s step : %s",
                    authenticator, stepNo));
        }
    }

    /**
     * This method is used to trigger exsisting event handlers from the eventing framework.
     *
     * @param subscriberName                   Existing authenticatedSteps Object.
     * @param restAuthenticationContext        Current authentication context of the rest flow.
     * @throws AuthenticationClientException   Return the error message/code/description from the OOTP handler.
     */
    private void executeEvent(String subscriberName, RestAuthenticationContext restAuthenticationContext)
            throws AuthenticationClientException {

        Map<String, Object> eventProperties = new HashMap<>();
        Map<String, Object> eventPropertiesParams = new HashMap<>();

        AuthenticationContext authenticationContext = new AuthenticationContext();
        authenticationContext.setContextIdentifier(restAuthenticationContext.getFlowIdIdentifier());
        authenticationContext.setRelyingParty(restAuthenticationContext.getServiceProvider().getApplicationName());
        authenticationContext.setServiceProviderName(restAuthenticationContext.getServiceProvider()
                .getApplicationName());
        authenticationContext.setCurrentAuthenticator(restAuthenticationContext.getCurrentAuthenticator());

        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setApplicationId(authenticationContext.getRelyingParty());
        sequenceConfig.setAuthenticatedUser(restAuthenticationContext.getAuthenticatedUser());

        authenticationContext.setSequenceConfig(sequenceConfig);
        authenticationContext.setRequestType(Constants.REQUEST_TYPE);

        if (IdentityEventConstants.EventName.AUTHENTICATION_STEP_SUCCESS.toString().equals(subscriberName)) {
            authenticationContext.setCurrentStep(restAuthenticationContext.getAuthenticatedSteps().size());
            eventProperties.put(Constants.AUTHENTICATION_STATUS, AuthenticatorStatus.PASS);
        } else if (IdentityEventConstants.EventName.AUTHENTICATION_STEP_FAILURE.toString().equals(subscriberName)) {
            authenticationContext.setCurrentStep(restAuthenticationContext.getAuthenticatedSteps().size() + 1);
            eventProperties.put(Constants.AUTHENTICATION_STATUS, AuthenticatorStatus.FAIL);
        }

        eventProperties.put(Constants.CONTEXT, authenticationContext);
        eventPropertiesParams.put(Constants.USER, restAuthenticationContext.getAuthenticatedUser());
        eventPropertiesParams.put(Constants.IS_FEDERATED, false);

        eventProperties.put(Constants.PARAMS, eventPropertiesParams);
        Event event = new Event
                (subscriberName, eventProperties);

        try {
            ((IdentityEventService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(IdentityEventService.class, null)).handleEvent(event);
        } catch (IdentityEventException e) {
            LOG.error(e.getCause());
            String handlerError = e.getErrorCode() + ";" + e.getMessage() + ";"
                    + Constants.EVENT_HANDLER_ERROR_DESCRIPTION;
            throw RestAuthUtil.handleClientException(handlerError, restAuthenticationContext.getCurrentAuthenticator());
        }
    }

    /**
     * This method is used to get the federated authenticators configured for a certain SP.
     *
     * @param authenticatorName         Name of the federated authenticator (SMSOTP/EmailOTP)
     * @param authContext               Current authentication context of the rest flow.
     * @throws AuthenticationException  Returns an AuthenticationException.
     * @return String                   Identity provider's federated IdP channel name.
     */
    private String getFederatedIdpChannelName(String authenticatorName, RestAuthenticationContext authContext)
            throws AuthenticationException {

        IdentityProvider[] federatedAuthenticators =
                RestAuthUtil.getFederatedIdentityProviders(authContext.getServiceProvider().getApplicationID())
                        .get(authContext.getNextAuthenticationStepId());

        for (IdentityProvider idp : federatedAuthenticators) {
            if (authenticatorName.equals(idp.getIdentityProviderName())) {
                return idp.getDefaultAuthenticatorConfig().getName();
            }
        }
        return null;
    }

    /**
     * This method is used to get user's channel identifier attribute.
     *
     * @param userId                          User's user id.
     * @param userTenantId                    User's tenant id.
     * @param requestedClaimURI               Required channel Identifier's claim URI.
     * @throws AuthenticationServerException  Returns an AuthenticationException.
     * @return String                         User's chanel Identifier's value.
     */
    private String getUserVerificationChannel(String userId, int userTenantId, String requestedClaimURI)
            throws AuthenticationServerException {

        AbstractUserStoreManager userStoreManager;
        Map<String, String> userAttributes;
        try {
            userStoreManager = (AbstractUserStoreManager) AuthenticationServiceDataHolder.getInstance()
                    .getRealmService().getTenantUserRealm(userTenantId).getUserStoreManager();
            userAttributes = userStoreManager.getUserWithID(userId, new String[]{requestedClaimURI},
                    UserCoreConstants.DEFAULT_PROFILE).getAttributes();

            if(LOG.isDebugEnabled()) {
                LOG.debug("Verifier claim value resolved for the user id : " + userId);
            }

        } catch (UserStoreException e) {
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_USER_STORE_MANAGER_ERROR,
                    "Error while resolving the user's channel identifier from user store.", e);
        }
        return userAttributes.get(requestedClaimURI);
    }
}
