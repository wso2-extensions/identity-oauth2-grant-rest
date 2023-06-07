package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.*;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/mfa")
@Api(description = "The mfa API")
public class MFAApi {


    @Autowired
    private MFAApiService delegate;

    @Valid
    @POST
    @Path("/initialize")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "This API is used to generate the otp.  <b>Permission required:</b> <br>  * none <br>  <b>Scope required:</b> <br>  * internal_login ", response = MFAInitializationResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "MFA", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MFAInitializationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = MFAError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
            @ApiResponse(code = 500, message = "Server Error", response = MFAError.class)
    })
    public Response mfaInitializePost(@ApiParam(value = "" ,required=true) @Valid MFAInitializationRequest mfaInitializationRequest) {

        return delegate.mfaInitializePost(mfaInitializationRequest );
    }

    @Valid
    @POST
    @Path("/validate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "This API is used to validate the otp.  <b>Permission required:</b> <br>  * none <br>  <b>Scope required:</b> <br>  * internal_login ", response = MFAValidationResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "MFA" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MFAValidationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = MFAError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
            @ApiResponse(code = 500, message = "Server Error", response = MFAError.class)
    })
    public Response mfaValidatePost(@ApiParam(value = "" ,required=true) @Valid MFAValidationRequest mfaValidationRequest) {

        return delegate.mfaValidatePost(mfaValidationRequest);
    }

}
