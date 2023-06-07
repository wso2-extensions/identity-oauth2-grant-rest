package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.util;

import org.apache.commons.logging.Log;
import org.slf4j.MDC;
import org.wso2.carbon.context.PrivilegedCarbonContext;

import javax.ws.rs.core.Response;
import java.util.UUID;

import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAError;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception.ConflictRequestException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthService;
import static org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.constant.Constants.CORRELATION_ID_MDC;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.exception.*;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;



/**
 * This class provides util functions for MFA Auth REST APIs.
 */
public class EndpointUtils {
	public static MFAAuthService getMFAAuthService() {

		return (MFAAuthService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
				getOSGiService(MFAAuthService.class, null);
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
			ref = MDC.get(CORRELATION_ID_MDC).toString();
		} else {
			ref = UUID.randomUUID().toString();
		}
		return ref;
	}

	public static boolean isCorrelationIDPresent() {
		return MDC.get(CORRELATION_ID_MDC) != null;
	}

	private static MFAError getError(String authenticator, String message, String description, String code) {

		MFAError errorDTO = new MFAError();
		errorDTO.setAuthenticator(authenticator);
		errorDTO.setCode(code);
		errorDTO.setMessage(message);
		errorDTO.setDescription(description);
		errorDTO.setTraceId(getCorrelation());
		return errorDTO;
	}

	public static Response handleBadRequestResponse(String authenticator, MFAAuthClientException e, Log log) {

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

	public static Response handleServerErrorResponse(String authenticator, MFAAuthException e, Log log) {

		throw buildInternalServerErrorException(authenticator, e.getErrorCode(), log, e);
	}

	public static Response handleUnexpectedServerError(String authenticator,Throwable e, Log log) {

		throw buildInternalServerErrorException(authenticator,
				Constants.ErrorMessage.SERVER_UNEXPECTED_ERROR.getCode(), log, e);
	}

	private static boolean isNotFoundError(MFAAuthClientException e) {

		return Constants.isNotFoundError(e.getErrorCode());
	}

	private static boolean isConflictError(MFAAuthClientException e) {

		return Constants.isConflictError(e.getErrorCode());
	}

	private static boolean isForbiddenError(MFAAuthClientException e) {

		return Constants.isForbiddenError(e.getErrorCode());
	}

	public static NotFoundException buildNotFoundRequestException(String authenticator, String description,
																  String message, String code,
																  Log log, Throwable e) {

		MFAError errorDTO = getError(authenticator, message, description, code);
		logDebug(log, e);
		return new NotFoundException(errorDTO);
	}

	public static ForbiddenException buildForbiddenException(String authenticator, String description, String message
			, String code, Log log,
															 Throwable e) {

		MFAError errorDTO = getError(authenticator, message, description, code);
		logDebug(log, e);
		return new ForbiddenException(errorDTO);
	}

	public static BadRequestException buildBadRequestException(String authenticator, String description,
															   String message, String code, Log log,
															   Throwable e) {

		MFAError errorDTO = getError(authenticator, message, description, code);
		logDebug(log, e);
		return new BadRequestException(errorDTO);
	}

	public static InternalServerErrorException buildInternalServerErrorException(String authenticator, String code,
																				 Log log, Throwable e) {

		MFAError errorDTO = getError(authenticator, Response.Status.INTERNAL_SERVER_ERROR.toString(),
				Response.Status.INTERNAL_SERVER_ERROR.toString(), code);
		logError(log, e);
		return new InternalServerErrorException(errorDTO);
	}

	public static ConflictRequestException buildConflictRequestException(String authenticator, String description,
																		 String message,
																		 String code, Log log, Throwable e) {

		MFAError errorDTO = getError(authenticator, message, description, code);
		logDebug(log, e);
		return new ConflictRequestException(errorDTO);
	}
}
