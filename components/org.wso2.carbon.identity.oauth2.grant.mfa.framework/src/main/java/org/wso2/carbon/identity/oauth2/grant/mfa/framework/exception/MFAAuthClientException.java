package org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception;


/**
 * This class is to handle MFA Auth client exception.
 */
public class MFAAuthClientException extends MFAAuthException {

	public MFAAuthClientException(String errorCode, String message) {

		super(errorCode, message);
	}

	public MFAAuthClientException(String errorCode, String message, Throwable throwable) {

		super(errorCode, message, throwable);
	}

	public MFAAuthClientException(String errorCode, String message, String description) {

		super(errorCode, message, description);
	}

	public MFAAuthClientException(String errorCode, String message, String description, Throwable e) {

		super(errorCode, message, description, e);
	}
}
