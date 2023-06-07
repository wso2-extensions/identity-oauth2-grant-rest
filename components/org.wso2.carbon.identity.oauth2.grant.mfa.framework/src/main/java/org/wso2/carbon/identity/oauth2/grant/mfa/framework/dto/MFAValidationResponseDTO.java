package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

import java.util.LinkedHashMap;

/**
 * This class represents a model to validate OTP response.
 */
public class MFAValidationResponseDTO {

	private boolean isValidOTP;
	private String userId;
	private String mfaToken;
	private boolean isAuthFlowCompleted;
	private LinkedHashMap<Integer, String> authenticatedSteps;
	private Object authenticationSteps;
	private int nextStep;
	private MFAValidationFailureReasonDTO failureReason;


	public boolean isValidOTP() {
		return isValidOTP;
	}

	public MFAValidationResponseDTO setValid(boolean valid) {
		this.isValidOTP = valid;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public MFAValidationResponseDTO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getMfaToken() {
		return mfaToken;
	}

	public MFAValidationResponseDTO setMfaToken(String mfaToken) {
		this.mfaToken = mfaToken;
		return this;
	}

	public boolean isAuthFlowCompleted() {
		return isAuthFlowCompleted;
	}

	public MFAValidationResponseDTO setAuthFlowCompleted(boolean authFlowCompleted) {
		this.isAuthFlowCompleted = authFlowCompleted;
		return this;
	}

	public LinkedHashMap<Integer, String> getAuthenticatedSteps() {
		return authenticatedSteps;
	}

	public MFAValidationResponseDTO setAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {
		this.authenticatedSteps = authenticatedSteps;
		return this;
	}

	public Object getAuthenticationSteps() {
		return authenticationSteps;
	}

	public MFAValidationResponseDTO setAuthenticationSteps(Object authenticationSteps) {
		this.authenticationSteps = authenticationSteps;
		return this;
	}

	public int getNextStep() {
		return nextStep;
	}

	public MFAValidationResponseDTO setNextStep(int nextStep) {
		this.nextStep = nextStep;
		return this;
	}

	public MFAValidationFailureReasonDTO getFailureReason() {
		return failureReason;
	}

	public MFAValidationResponseDTO setFailureReason(MFAValidationFailureReasonDTO failureReason) {
		this.failureReason = failureReason;
		return this;
	}

}
