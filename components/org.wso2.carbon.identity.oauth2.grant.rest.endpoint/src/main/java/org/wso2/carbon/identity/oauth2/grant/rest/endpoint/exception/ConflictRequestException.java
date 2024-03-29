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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception;

import org.apache.http.HttpHeaders;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationError;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class is to handle Bad ConflictRequest Exceptions.
 */
public class ConflictRequestException extends WebApplicationException {

    private String message;
    public ConflictRequestException(AuthenticationError error) {

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
