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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationStepError;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationStepsResponse;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.AuthStepsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/auth-steps")
@Api(description = "The auth-steps API")

public class AuthStepsApi  {

    @Autowired
    private AuthStepsApiService delegate;

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "This API context can be used to retrieve the configured authentication steps of a given OAuth2/OpenId Connect Service Provider. The service provider is identified via the clientId.  The API endpoints specify a set of error scenarios which can be found at WSO2 official documentation for this connector. ### <b>Permission required:</b>  * none  ### <b>Scope required:</b> * none ", response = AuthenticationStepsResponse.class, tags={ "Restful Authentication API" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = AuthenticationStepsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = AuthenticationStepError.class),
        @ApiResponse(code = 404, message = "Not Found", response = AuthenticationStepError.class),
        @ApiResponse(code = 500, message = "Server Error", response = AuthenticationStepError.class)
    })
    public Response authStepsGet(    @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "The Client ID of the Oauth2/OpenId configuration.",required=true)  @QueryParam("clientId") String clientId) {

        return delegate.authStepsGet(clientId );
    }

}
