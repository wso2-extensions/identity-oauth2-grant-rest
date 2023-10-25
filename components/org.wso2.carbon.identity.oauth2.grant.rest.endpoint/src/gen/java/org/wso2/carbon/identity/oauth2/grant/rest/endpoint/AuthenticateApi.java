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

import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationError;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationValidationRequest;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationValidationResponse;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.AuthenticateApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/authenticate")
@Api(description = "The authenticate API")

public class AuthenticateApi  {

    @Autowired
    private AuthenticateApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "This API context can be used to process an authentication request and get an appropriate response.</br> ### This API can be used to fullfill below two tasks,</br> 1. Initialize the authentication with client ID via Basic Authentication and Idenfier First.</br>  2. Process the authentication request of the selected authenticator.</br>  The API endpoints specify a set of error scenarios which can be found at WSO2 official documentation for this connector. ### <b>Permission required:</b> * none <br>  ### <b>Scope required:</b> * none ", response = AuthenticationValidationResponse.class, tags={ "Restful Authentication API" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = AuthenticationValidationResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = AuthenticationError.class),
        @ApiResponse(code = 404, message = "Not Found", response = AuthenticationError.class),
        @ApiResponse(code = 500, message = "Server Error", response = AuthenticationError.class)
    })
    public Response authenticatePost(@ApiParam(value = "" ,required=true) @Valid AuthenticationValidationRequest authenticationValidationRequest) {

        return delegate.authenticatePost(authenticationValidationRequest );
    }

}
