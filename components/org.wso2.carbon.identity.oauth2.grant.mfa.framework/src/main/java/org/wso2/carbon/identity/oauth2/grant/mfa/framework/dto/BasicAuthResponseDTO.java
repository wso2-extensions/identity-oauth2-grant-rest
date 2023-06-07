package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

/**
 * This class represents a model to generate Basic Authentication response.
 */
public class BasicAuthResponseDTO {

	private String mfaToken;
	private Object authenticationSteps;
	private Object authenticatedSteps;
	private boolean isAuthFlowCompleted = false;
	private int nextStep;
	private String authenticator;

	public String getMfaToken() {
		return mfaToken;
	}
	public void setMfaToken(String mfaToken) {
		this.mfaToken = mfaToken;
	}

	public Object getAuthenticationSteps() {
		return authenticationSteps;
	}
	public void setAuthenticationSteps(Object authenticationSteps) {
		this.authenticationSteps = authenticationSteps;
	}

	public Object getAuthenticatedSteps() {
		return authenticatedSteps;
	}
	public void setAuthenticatedSteps(Object authenticatedSteps) {
		this.authenticatedSteps = authenticatedSteps;
	}

	public int getNextStep() {
		return nextStep;
	}
	public void setNextStep(int nextStep) {
		this.nextStep = nextStep;
	}

	public boolean isAuthFlowCompleted() {return isAuthFlowCompleted; }
	public void setAuthFlowCompleted(boolean isAuthFlowCompleted) {this.isAuthFlowCompleted = isAuthFlowCompleted; }

	public String getAuthenticator() {
		return authenticator;
	}
	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}
}
