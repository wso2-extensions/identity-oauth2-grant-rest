package org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception;

import org.wso2.carbon.identity.event.IdentityEventException;


/**
 * This class is to handle MFA Auth Exception.
 */
public class MFAAuthException extends IdentityEventException {

	private String errorCode;
	private String message;
	private String description;

	public MFAAuthException(String message, Throwable throwable) {

		super(message, throwable);
		this.message = message;
	}

	public MFAAuthException(String errorCode, String message) {

		super(errorCode, message);
		this.errorCode = errorCode;
		this.message = message;
	}

	public MFAAuthException(String errorCode, String message, Throwable throwable) {

		super(errorCode, message, throwable);
		this.errorCode = errorCode;
		this.message = message;
	}

	public MFAAuthException(String errorCode, String message, String description) {

		super(errorCode, message);
		this.errorCode = errorCode;
		this.message = message;
		this.description = description;
	}

	public MFAAuthException(String errorCode, String message, String description, Throwable e) {

		super(errorCode, message, e);
		this.errorCode = errorCode;
		this.message = message;
		this.description = description;
	}

	@Override
	public String getErrorCode() {

		return errorCode;
	}

	@Override
	public String getMessage() {

		return message;
	}

	public String getDescription() {

		return description;
	}
}
