package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

/**
 * This class represents a model to generate OTP request.
 */
public class MFAInitializationResponseDTO {

	private String mfaToken;
	private String authenticator;
	private String otp;

	public String getMfaToken() {
		return mfaToken;
	}
	public void setMfaToken(String mfaToken) {
		this.mfaToken = mfaToken;
	}

	public String getAuthenticator() {
		return authenticator;
	}
	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}

	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
}
