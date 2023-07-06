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
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.AuthStepsApiService;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationStepsResponse;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RestAuthenticationResponseBuilder;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.RestEndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticationStepsResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import javax.ws.rs.core.Response;

/**
 *This class is used to get authentications steps configured to the given Service Provider.
 */
public class AuthStepsApiServiceImpl implements AuthStepsApiService {

    private static final Log LOG = LogFactory.getLog(AuthStepsApiServiceImpl.class);
    @Override
    public Response authStepsGet(String clientId) {

        try {
            AuthenticationStepsResponseDTO responseDTO = RestEndpointUtils
                    .getAuthService()
                    .getAuthenticationStepsFromSP(clientId);

            AuthenticationStepsResponse response =
                    new AuthenticationStepsResponse().authenticationSteps
                            (RestAuthenticationResponseBuilder.buildAuthenticationStepsResponse
                                    (responseDTO.getAuthenticationSteps()));

            return Response.ok(response).build();

        } catch (AuthenticationClientException e) {
            return RestEndpointUtils.handleBadRequestResponse(clientId, e, LOG);
        } catch (AuthenticationException e) {
            return RestEndpointUtils.handleServerErrorResponse(clientId, e, LOG);
        } catch (Throwable e) {
            return RestEndpointUtils.handleUnexpectedServerError(clientId, e, LOG);
        }
    }
}
