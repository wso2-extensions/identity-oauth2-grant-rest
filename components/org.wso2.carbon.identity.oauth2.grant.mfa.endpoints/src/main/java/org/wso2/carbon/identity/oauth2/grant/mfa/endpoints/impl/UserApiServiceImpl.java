package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.UserApiService;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.util.EndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.BasicAuthResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

import javax.ws.rs.core.Response;

public class UserApiServiceImpl implements UserApiService {

    private static final Log log = LogFactory.getLog(UserApiServiceImpl.class);

    @Override
    public Response userAuthenticatePost(UserAuthenticationRequest userAuthenticationRequest, String userTenantDomain) {

        String username = StringUtils.trim(userAuthenticationRequest.getUsername());
        String password = StringUtils.trim(userAuthenticationRequest.getPassword());
        String clientId = StringUtils.trim(userAuthenticationRequest.getClientId());
        String authenticator = StringUtils.isEmpty(password)?
                Constants.AUTHENTICATOR_NAME_IDENTIFIER_FIRST :
                Constants.AUTHENTICATOR_NAME_BASIC_AUTH;

        try {
            BasicAuthResponseDTO responseDTO = EndpointUtils.getMFAAuthService().authenticatefirstStep(username,
                    password, clientId, StringUtils.trim(userTenantDomain));
            UserAuthenticationResponse response = new UserAuthenticationResponse()
                    .mfaToken(responseDTO.getMfaToken())
                    .authenticationSteps(responseDTO.getAuthenticationSteps())
                    .authenticatedSteps(responseDTO.getAuthenticatedSteps())
                    .isAuthFlowCompleted(responseDTO.isAuthFlowCompleted())
                    .nextStep(responseDTO.getNextStep());
            return Response.ok(response).build();
        } catch (MFAAuthClientException e) {
            return EndpointUtils.handleBadRequestResponse(authenticator, e, log);
        } catch (MFAAuthException e) {
            return EndpointUtils.handleServerErrorResponse(authenticator, e, log);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(authenticator, e, log);
        }
    }
}
