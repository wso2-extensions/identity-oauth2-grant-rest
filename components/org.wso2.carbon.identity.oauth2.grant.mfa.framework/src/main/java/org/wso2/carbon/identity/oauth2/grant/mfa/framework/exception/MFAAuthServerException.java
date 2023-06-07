package org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception;

/**
 * This class is to handle MFA Auth server exception.
 */
public class MFAAuthServerException extends MFAAuthException {

	public MFAAuthServerException(String message, Throwable throwable) {

		super(message, throwable);
	}

	public MFAAuthServerException(String errorCode, String message) {

		super(errorCode, message);
	}

	public MFAAuthServerException(String errorCode, String message, Throwable throwable) {

		super(errorCode, message, throwable);
	}

	public MFAAuthServerException(String errorCode, String message, String description) {

		super(errorCode, message, description);
	}

	public MFAAuthServerException(String errorCode, String message, String description, Throwable e) {

		super(errorCode, message, description, e);
	}
}
