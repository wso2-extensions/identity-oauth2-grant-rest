package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception;

import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InternalServerErrorException extends WebApplicationException {

    public InternalServerErrorException(MFAError error) {

        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
    }
}
