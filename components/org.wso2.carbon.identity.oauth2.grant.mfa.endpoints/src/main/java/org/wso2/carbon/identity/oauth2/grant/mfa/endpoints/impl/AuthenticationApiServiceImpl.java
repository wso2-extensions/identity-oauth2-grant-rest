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

package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.AuthenticationApiService;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationInitializationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationInitializationResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationStepsRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationStepsResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationValidationFailureReason;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.util.EndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationValidationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.SPAuthneticationStepsDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.AuthenticationException;
import javax.ws.rs.core.Response;

public class AuthenticationApiServiceImpl implements AuthenticationApiService {
    private static final Log LOG = LogFactory.getLog(AuthenticationApiServiceImpl.class);

    @Override
    public Response initializePost
            (AuthenticationInitializationRequest authInitializationRequest, String userTenantDomain) {

        String authenticator = StringUtils.trim(authInitializationRequest.getAuthenticator());
        String flowId = StringUtils.trim(authInitializationRequest.getFlowId());

        if (LOG.isDebugEnabled()) {
           LOG.debug(String.format("Flow Id %$ initialized to authenticator %s", flowId, authenticator));
        }
        try {

            AuthenticationInitializationResponseDTO responseDTO = EndpointUtils.getMFAAuthService()
                    .initializeAuth(flowId, authenticator);
            AuthenticationInitializationResponse response = new AuthenticationInitializationResponse()
                    .flowId(responseDTO.getFlowId())
                    .authenticator(responseDTO.getAuthenticator());
                return Response.ok(response).build();

        } catch (AuthenticationClientException e) {
            return EndpointUtils.handleBadRequestResponse(authenticator, e, LOG);
        } catch (AuthenticationException e) {
            return EndpointUtils.handleServerErrorResponse(authenticator, e, LOG);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(authenticator, e, LOG);
        }
    }

    /**
     * @param userAuthenticationRequest 
     * @param userTenantDomain
     * @return
     */
    @Override
    public Response authenticatePost(UserAuthenticationRequest userAuthenticationRequest, String userTenantDomain) {

        String userIdentifier = StringUtils.trim(userAuthenticationRequest.getUserIdentifier());
        String password = StringUtils.trim(userAuthenticationRequest.getPassword());
        String clientId = StringUtils.trim(userAuthenticationRequest.getClientId());
        String flowId = StringUtils.trim(userAuthenticationRequest.getFlowId());
        String authenticator = StringUtils.trim(userAuthenticationRequest.getAuthenticator());
        UserAuthenticationResponseDTO responseDTO = null;

        try {

            if (StringUtils.isNotEmpty(StringUtils.trim(userAuthenticationRequest.getClientId())) &&
                    StringUtils.isEmpty(StringUtils.trim(userAuthenticationRequest.getFlowId())) &&
                    StringUtils.isNotEmpty(StringUtils.trim(userAuthenticationRequest.getUserIdentifier()))) {

                responseDTO = EndpointUtils.getMFAAuthService().authenticateWithClientId(clientId, authenticator,
                        password, userIdentifier, StringUtils.trim(userTenantDomain));

            } else if (StringUtils.isNotEmpty(StringUtils.trim(userAuthenticationRequest.getFlowId())) &&
                    StringUtils.isEmpty(StringUtils.trim(userAuthenticationRequest.getClientId()))) {

                responseDTO = EndpointUtils.getMFAAuthService().authenticateWithFlowId
                        (flowId, authenticator, password);
            }

            AuthenticationValidationFailureReasonDTO failureReasonDTO = responseDTO.getFailureReason();
            AuthenticationValidationFailureReason failureReason = null;
            if (failureReasonDTO != null) {
                failureReason = new AuthenticationValidationFailureReason()
                        .code(failureReasonDTO.getCode())
                        .message(failureReasonDTO.getMessage())
                        .description(failureReasonDTO.getDescription());
            }

            UserAuthenticationResponse response = new UserAuthenticationResponse()
                    .isValidPassword(responseDTO.isValidPassword())
                    .flowId(responseDTO.getFlowId())
                    .authenticatedSteps(responseDTO.getAuthenticatedSteps())
                    .isAuthFlowCompleted(responseDTO.isAuthFlowCompleted())
                    .setAutheticatorStepDetails(responseDTO.getAuthenticationStepDetails())
                    .nextStep(responseDTO.getNextStep())
                    .failureReason(failureReason);
            return Response.ok(response).build();
        } catch (AuthenticationClientException e) {
            return EndpointUtils.handleBadRequestResponse(authenticator, e, LOG);
        } catch (AuthenticationException e) {
            return EndpointUtils.handleServerErrorResponse(authenticator, e, LOG);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(authenticator, e, LOG);
        }
    }

    /**
     * @param authenticationStepsRequest
     * @return
     */
    @Override
    public Response getSPAuthenticationSteps(AuthenticationStepsRequest authenticationStepsRequest) {

        String clientId = StringUtils.trim(authenticationStepsRequest.getClientId());
        try {
            SPAuthneticationStepsDTO responseDTO = EndpointUtils
                    .getMFAAuthService()
                    .getAuthenticationStepsFromSP(clientId);

            AuthenticationStepsResponse response = new AuthenticationStepsResponse()
                    .setAutheticatorStepDetails(responseDTO.getAuthenticationStepContextList());

            return Response.ok(response).build();

        } catch (AuthenticationClientException e) {
            return EndpointUtils.handleBadRequestResponse(clientId, e, LOG);
        } catch (AuthenticationException e) {
            return EndpointUtils.handleServerErrorResponse(clientId, e, LOG);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(clientId, e, LOG);
        }
    }


}
