package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception;

import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {

    private String message;

    public NotFoundException(MFAError error) {

        super(Response.status(Response.Status.NOT_FOUND).entity(error)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
        message = error.getDescription();
    }

    public NotFoundException() {

        super(Response.Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {

        return message;
    }
}
