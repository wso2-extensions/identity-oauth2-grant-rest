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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.apache.commons.logging.Log;
import org.slf4j.MDC;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception.BadRequestException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception.ConflictRequestException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception.ForbiddenException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception.InternalServerErrorException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.exception.NotFoundException;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationError;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.RestAuthenticationService;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import java.util.UUID;
import javax.ws.rs.core.Response;
import static org.wso2.carbon.identity.oauth2.grant.rest.endpoint.constant.Constants.CORRELATION_ID_MDC;

/**
 * This class provides util functions for Auth REST APIs.
 */
public class RestEndpointUtils {
    public static RestAuthenticationService getAuthService() {

        return (RestAuthenticationService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                getOSGiService(RestAuthenticationService.class, null);
    }

    private static void logDebug(Log log, Throwable throwable) {

        if (log.isDebugEnabled()) {
            log.debug(Response.Status.BAD_REQUEST, throwable);
        }
    }

    private static void logError(Log log, Throwable throwable) {

        log.error(throwable.getMessage(), throwable);
    }

    public static String getCorrelation() {

        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(CORRELATION_ID_MDC);
        } else {
            ref = UUID.randomUUID().toString();
        }
        return ref;
    }

    public static boolean isCorrelationIDPresent() {
        return MDC.get(CORRELATION_ID_MDC) != null;
    }

    private static AuthenticationError getError(String authenticator, String message, String description, String code) {

        AuthenticationError errorDTO = new AuthenticationError();
        errorDTO.setAuthenticator(authenticator);
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        errorDTO.setTraceId(getCorrelation());
        return errorDTO;
    }

    public static Response handleBadRequestResponse(String authenticator, AuthenticationClientException e, Log log) {

        if (isNotFoundError(e)) {
            throw buildNotFoundRequestException(authenticator, e.getDescription(), e.getMessage(), e.getErrorCode(),
                    log, e);
        }

        if (isConflictError(e)) {
            throw buildConflictRequestException(authenticator, e.getDescription(), e.getMessage(), e.getErrorCode(),
                    log, e);
        }

        if (isForbiddenError(e)) {
            throw buildForbiddenException(authenticator, e.getDescription(), e.getMessage(), e.getErrorCode(), log, e);
        }
        throw buildBadRequestException(authenticator, e.getDescription(), e.getMessage(), e.getErrorCode(), log, e);
    }

    public static Response handleServerErrorResponse(String authenticator, AuthenticationException e, Log log) {

        throw buildInternalServerErrorException(authenticator, e.getErrorCode(), log, e);
    }

    public static Response handleUnexpectedServerError(String authenticator, Throwable e, Log log) {

        throw buildInternalServerErrorException(authenticator,
                Constants.ErrorMessage.SERVER_UNEXPECTED_ERROR.getCode(), log, e);
    }

    private static boolean isNotFoundError(AuthenticationClientException e) {

        return Constants.isNotFoundError(e.getErrorCode());
    }

    private static boolean isConflictError(AuthenticationClientException e) {

        return Constants.isConflictError(e.getErrorCode());
    }

    private static boolean isForbiddenError(AuthenticationClientException e) {

        return Constants.isForbiddenError(e.getErrorCode());
    }

    public static NotFoundException buildNotFoundRequestException(String authenticator, String description,
                                                                  String message, String code,
                                                                  Log log, Throwable e) {

        AuthenticationError errorDTO = getError(authenticator, message, description, code);
        logDebug(log, e);
        return new NotFoundException(errorDTO);
    }

    public static ForbiddenException buildForbiddenException(String authenticator, String description, String message
            , String code, Log log, Throwable e) {

        AuthenticationError errorDTO = getError(authenticator, message, description, code);
        logDebug(log, e);
        return new ForbiddenException(errorDTO);
    }

    public static BadRequestException buildBadRequestException(String authenticator, String description,
                                                               String message, String code, Log log,
                                                               Throwable e) {

        AuthenticationError errorDTO = getError(authenticator, message, description, code);
        logDebug(log, e);
        return new BadRequestException(errorDTO);
    }

    public static InternalServerErrorException buildInternalServerErrorException(String authenticator, String code,
                                                                                 Log log, Throwable e) {

        AuthenticationError errorDTO = getError(authenticator, Response.Status.INTERNAL_SERVER_ERROR.toString(),
                Response.Status.INTERNAL_SERVER_ERROR.toString(), code);
        logError(log, e);
        return new InternalServerErrorException(errorDTO);
    }

    public static ConflictRequestException buildConflictRequestException(String authenticator, String description,
                                                                         String message,
                                                                         String code, Log log, Throwable e) {

        AuthenticationError errorDTO = getError(authenticator, message, description, code);
        logDebug(log, e);
        return new ConflictRequestException(errorDTO);
    }
}
