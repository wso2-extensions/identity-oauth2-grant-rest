package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception;

import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ConflictRequestException extends WebApplicationException {

    private String message;
    public ConflictRequestException(MFAError error) {

        super(Response.status(Response.Status.CONFLICT).entity(error)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
        message = error.getDescription();
    }

    public ConflictRequestException() {

        super(Response.Status.CONFLICT);
    }

    @Override
    public String getMessage() {

        return message;
    }
}
