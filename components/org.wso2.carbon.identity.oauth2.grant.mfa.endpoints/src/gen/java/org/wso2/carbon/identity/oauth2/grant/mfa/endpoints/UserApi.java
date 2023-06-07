package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationError;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationResponse;


import javax.validation.Valid;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/user")
@Api(description = "The user API")
public class UserApi {
    @Autowired
    private UserApiService delegate;

    @Valid
    @POST
    @Path("/authenticate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "This API is used to authenticate the user using user credentials  and to get a temporary JWT token which can be used to know the  next authentication step.  <b>Permission required:</b> <br>  * none <br>  <b>Scope required:</b> <br>  * internal_login ", response = UserAuthenticationResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "Basic Auth" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserAuthenticationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = UserAuthenticationError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
            @ApiResponse(code = 500, message = "Server Error", response = UserAuthenticationError.class)
    })
    public Response userAuthenticatePost(@ApiParam(value = "" ,required=true) @Valid UserAuthenticationRequest userAuthenticationRequest, @Valid@ApiParam(value = "Tenant Domain of the user")  @QueryParam("userTenantDomain") String userTenantDomain) {

        return delegate.userAuthenticatePost(userAuthenticationRequest, userTenantDomain );
    }

}
