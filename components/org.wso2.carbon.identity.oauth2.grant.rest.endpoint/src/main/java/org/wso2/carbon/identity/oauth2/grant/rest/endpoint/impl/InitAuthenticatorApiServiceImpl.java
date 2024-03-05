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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticationInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.InitAuthenticatorApiService;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.builder.ErrorBuilder;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorInitializationRequest;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorInitializationResponse;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ConfigUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ErrorUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RequestSanitizerUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RestEndpointUtils;
import javax.ws.rs.core.Response;

/**
 *This class is used to initialize the authentication flow.
 */
public class InitAuthenticatorApiServiceImpl implements InitAuthenticatorApiService {

    private static final Log LOG = LogFactory.getLog(InitAuthenticatorApiServiceImpl.class);
    @Override
    public Response initAuthenticatorPost(AuthenticatorInitializationRequest authenticatorInitializationRequest) {

        AuthenticatorInitializationRequest trimmedRequest = RequestSanitizerUtil.trimInitializationRequestPayload
                (authenticatorInitializationRequest);
        String authenticator = trimmedRequest.getAuthenticator();
        String flowId = trimmedRequest.getFlowId();

        try {

            AuthenticationInitializationResponseDTO responseDTO = RestEndpointUtils.getAuthService()
                    .executeAuthStep(flowId, authenticator);
            AuthenticatorInitializationResponse response = new AuthenticatorInitializationResponse()
                    .flowId(responseDTO.getFlowId())
                    .authenticator(responseDTO.getAuthenticator())
                    .userChannelIdentifierClaim(responseDTO.getUserChannelIdentifierClaim());
            return Response.ok(response).build();

        } catch (AuthenticationClientException e) {

            if (ConfigUtil.isIsPropertyFileAvailable()) {
                ErrorUtil errorUtil = ErrorBuilder.buildError(e);
                e = new AuthenticationClientException(
                        errorUtil.getErrorCode(),
                        errorUtil.getErrorMessage(),
                        errorUtil.getErrorDescription()
                );
            }
            return RestEndpointUtils.handleBadRequestResponse(authenticator, e, LOG);
        } catch (AuthenticationException e) {
            return RestEndpointUtils.handleServerErrorResponse(authenticator, e, LOG);
        } catch (Throwable e) {
            return RestEndpointUtils.handleUnexpectedServerError(authenticator, e, LOG);
        }
    }
}