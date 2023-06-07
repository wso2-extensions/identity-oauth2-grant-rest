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

package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationError;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationInitializationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationInitializationResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationStepsRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.AuthenticationStepsResponse;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationError;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/")
@Api(description = "The Authentication Flow API")
public class AuthenticationApi {

    @Autowired
    private AuthenticationApiService delegate;

    @Valid
    @POST
    @Path("/init-authenticator")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "", response = AuthenticationInitializationResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = { "REST Auth" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response",
                    response = AuthenticationInitializationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = AuthenticationError.class),
            @ApiResponse(code = 500, message = "Server Error", response = AuthenticationError.class)
    })
    public Response stepInitializePost(
            @ApiParam(value = "" , required = true)
            @Valid AuthenticationInitializationRequest authInitializationRequest,
            @Valid@ApiParam(value = "Tenant Domain of the user")
            @QueryParam("userTenantDomain") String userTenantDomain) {

        return delegate.initializePost(authInitializationRequest, userTenantDomain);
    }

    @Valid
    @POST
    @Path("/authenticate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "", response = UserAuthenticationResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = { "REST Auth" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserAuthenticationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = UserAuthenticationError.class),
            @ApiResponse(code = 500, message = "Server Error", response = UserAuthenticationError.class)
    })
    public Response userAuthenticatePost(@ApiParam(value = "" , required = true)
                                         @Valid UserAuthenticationRequest userAuthenticationRequest,
                                         @Valid@ApiParam(value = "Tenant Domain of the user")
                                         @QueryParam("userTenantDomain") String userTenantDomain) {

        return delegate.authenticatePost(userAuthenticationRequest, userTenantDomain);
    }

    @Valid
    @GET
    @Path("/auth-steps")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the authenticated steps by client id.",
            notes = "", response = AuthenticationStepsResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = { "REST Auth" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = AuthenticationStepsResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = AuthenticationError.class),
            @ApiResponse(code = 500, message = "Server Error", response = AuthenticationError.class)
    })

    public Response getAuthSteps(
            @ApiParam(value = "clientId", required = true)
            @Valid AuthenticationStepsRequest authenticationStepsRequest,
            @RequestParam(value = "clientId") String clientId) {

        return delegate.getSPAuthenticationSteps(authenticationStepsRequest);
    }

}
