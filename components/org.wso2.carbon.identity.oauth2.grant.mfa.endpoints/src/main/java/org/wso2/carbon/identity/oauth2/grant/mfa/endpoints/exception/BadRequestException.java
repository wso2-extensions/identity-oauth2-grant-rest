package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception;

import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BadRequestException extends WebApplicationException  {

    private String message;

    public BadRequestException(MFAError error) {

        super(Response.status(Response.Status.BAD_REQUEST).entity(error)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
        message = error.getDescription();
    }

    public BadRequestException() {

        super(Response.Status.BAD_REQUEST);
    }

    @Override
    public String getMessage() {

        return message;
    }
}
