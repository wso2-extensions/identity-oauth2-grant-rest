package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;


import org.apache.commons.lang3.StringUtils;
import org.wso2.carbon.identity.smsotp.common.constant.Constants;

/**
 * This class represents the model of the OTP validation
 * failure reason message, if any.
 */
public class MFAValidationFailureReasonDTO {

	String code;
	String message;
	String description;
	int remainingAttempts;

	public MFAValidationFailureReasonDTO(String code, String message, String description) {

		this.code = code;
		this.message = message;
		this.description = description;
		this.remainingAttempts = 0;
	}

	public MFAValidationFailureReasonDTO(String code, String message, String description, int remainingAttempts) {

		this.code = code;
		this.message = message;
		this.description = description;
		this.remainingAttempts = remainingAttempts;
	}

	public MFAValidationFailureReasonDTO(Constants.ErrorMessage error, String data) {

		this.code = error.getCode();
		this.message = error.getMessage();
		description = StringUtils.isNotBlank(data) ? String.format(error.getDescription(), data)
				: error.getDescription();
		this.remainingAttempts = 0;
	}

	/**
	 * This method composes the error message for failed mfa validation attempts.
	 *
	 * @param error                     Error message.
	 * @param data                      Data passed for the string argument in error message.
	 * @param remainingFailedAttempts   No of remaining validation attempts.
	 */
	public MFAValidationFailureReasonDTO(Constants.ErrorMessage error, String data, int remainingFailedAttempts) {

		this.code = error.getCode();
		this.message = error.getMessage();
		description = StringUtils.isNotBlank(data) ? String.format(error.getDescription(), data)
				: error.getDescription();
		this.remainingAttempts = remainingFailedAttempts;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public int getRemainingAttempts() {

		return remainingAttempts;
	}

	public void setRemainingAttempts(int remainingAttempts) {

		this.remainingAttempts = remainingAttempts;
	}

}
