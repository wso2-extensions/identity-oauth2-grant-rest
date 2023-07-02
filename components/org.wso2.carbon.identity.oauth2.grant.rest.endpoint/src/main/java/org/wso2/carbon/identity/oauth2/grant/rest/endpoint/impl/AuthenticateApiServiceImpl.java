/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.*;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.*;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.EndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RequestSnatizerUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;

import java.util.List;
import javax.ws.rs.core.Response;

public class AuthenticateApiServiceImpl implements AuthenticateApiService {

    private static final Log LOG = LogFactory.getLog(AuthenticateApiServiceImpl.class);

    @Override
    public Response authenticatePost(AuthenticationValidationRequest authenticationValidationRequest) {

        String userIdentifier = RequestSnatizerUtil.trimString(authenticationValidationRequest.getUserIdentifier());
        String password = RequestSnatizerUtil.trimString(authenticationValidationRequest.getPassword());
        String clientId = RequestSnatizerUtil.trimString(authenticationValidationRequest.getClientId());
        String flowId = RequestSnatizerUtil.trimString(authenticationValidationRequest.getFlowId());
        String authenticator = RequestSnatizerUtil.trimString(authenticationValidationRequest.getAuthenticator());
        UserAuthenticationResponseDTO responseDTO = null;

        try{
            //TODO: if authenticator is not avialble in the request body, flow should terminate immediately.
           // if the authentication flow initialize
            if(RequestSnatizerUtil.isNotEmpty(clientId) && RequestSnatizerUtil.isNotEmpty(userIdentifier) &&
                    RequestSnatizerUtil.isEmpty(flowId)) {
                responseDTO = EndpointUtils.getAuthService().initializeAuthFlow(clientId, authenticator, password,
                        userIdentifier, "carbon.super");
            } else if (RequestSnatizerUtil.isNotEmpty(flowId) && RequestSnatizerUtil.isNotEmpty(userIdentifier) &&
                    RequestSnatizerUtil.isEmpty(clientId)) {
                responseDTO = EndpointUtils.getAuthService().processAuthStepResponse(flowId, authenticator, password);
            }

            AuthenticationFailureReasonDTO failureReasonDTO = responseDTO.getFailureReason();
            AuthenticationFailureReason failureReason = null;
            if (failureReasonDTO != null) {
                failureReason = new AuthenticationFailureReason()
                        .code(failureReasonDTO.getCode())
                        .message(failureReasonDTO.getMessage())
                        .description(failureReasonDTO.getDescription());
            }

            AuthenticationValidationResponse response = new AuthenticationValidationResponse()
                    .isStepSuccess(responseDTO.isValidPassword())
                    .flowId(responseDTO.getFlowId())
                    .authenticatedSteps(null)
                    .authenticationSteps(null)
                    .isAuthFlowCompleted(responseDTO.isAuthFlowCompleted())
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
}
