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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.AuthenticateApiService;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.builder.ErrorBuilder;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationFailureReason;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationValidationRequest;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationValidationResponse;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ErrorUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RequestSanitizerUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RestAuthenticationResponseBuilder;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RestEndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import javax.ws.rs.core.Response;

/**
 * This class is used to authenticate the user either with flowId or clientId.
 */
public class AuthenticateApiServiceImpl implements AuthenticateApiService {

    private static final Log LOG = LogFactory.getLog(AuthenticateApiServiceImpl.class);

    @Override
    public Response authenticatePost(AuthenticationValidationRequest authenticationValidationRequest) {

        String userIdentifier = RequestSanitizerUtil.trimString(authenticationValidationRequest.getUserIdentifier());
        String password = RequestSanitizerUtil.trimString(authenticationValidationRequest.getPassword());
        String clientId = RequestSanitizerUtil.trimString(authenticationValidationRequest.getClientId());
        String flowId = RequestSanitizerUtil.trimString(authenticationValidationRequest.getFlowId());
        String authenticator = RequestSanitizerUtil.trimString(authenticationValidationRequest.getAuthenticator());
        UserAuthenticationResponseDTO responseDTO = null;

        try {
            // if the authentication flow initialize
            if (RequestSanitizerUtil.isNotEmpty(clientId) && RequestSanitizerUtil.isNotEmpty(userIdentifier) &&
                    RequestSanitizerUtil.isEmpty(flowId)) {
                responseDTO = RestEndpointUtils.getAuthService().initializeAuthFlow(clientId, authenticator, password,
                        userIdentifier, PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            } else if (RequestSanitizerUtil.isNotEmpty(flowId) && RequestSanitizerUtil.isNotEmpty(password) &&
                    RequestSanitizerUtil.isEmpty(clientId)) {
                responseDTO = RestEndpointUtils.getAuthService().processAuthStepResponse
                        (flowId, authenticator, password);
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
                    .authenticatedSteps(RestAuthenticationResponseBuilder.buildPostAuthenticatedAuthenticatorResponse
                            (responseDTO.getAuthenticatedSteps()))
                    .authenticationSteps(RestAuthenticationResponseBuilder.buildAuthenticationStepsResponse
                            (responseDTO.getAuthenticationSteps()))
                    .isAuthFlowCompleted(responseDTO.isAuthFlowCompleted())
                    .nextStep(responseDTO.getNextStep())
                    .failureReason(failureReason);

            return Response.ok(response).build();

        } catch (AuthenticationClientException e) {
            ErrorUtil errorUtil = ErrorBuilder.buildError(e.getErrorCode());
            e = new AuthenticationClientException(
                    errorUtil.getErrorCode(),
                    errorUtil.getErrorMessage(),
                    errorUtil.getErrorDescription()
            );
            return RestEndpointUtils.handleBadRequestResponse(authenticator, e, LOG);
        } catch (AuthenticationException e) {
            return RestEndpointUtils.handleServerErrorResponse(authenticator, e, LOG);
        } catch (Throwable e) {
            return RestEndpointUtils.handleUnexpectedServerError(authenticator, e, LOG);
        }
    }
}
