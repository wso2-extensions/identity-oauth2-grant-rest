package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

/**
 * This class holds the SMS OTP feature configurations.
 */
public class ConfigsDTO {

	private boolean isEnabled;
	private boolean showFailureReason;
	private int mfaTokenValidityPeriod;
	private int timestampSkew;
	private String customLocalAuthenticatorName;



	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public boolean isShowFailureReason() { return showFailureReason; }

	public void setShowFailureReason(boolean showFailureReason) {
		this.showFailureReason = showFailureReason;
	}

	public int getMfaTokenValidityPeriod() {
		return mfaTokenValidityPeriod;
	}

	public void setMfaTokenValidityPeriod(int mfaTokenValidityPeriod) {
		this.mfaTokenValidityPeriod = mfaTokenValidityPeriod;
	}

	public int getTimestampSkew() { return timestampSkew;}

	public void setTimestampSkew(int timestampSkew) {
		this.timestampSkew = timestampSkew;
	}
	public void setCustomLocalAuthenticatorName(String customLocalAuthenticatorName){
		this.customLocalAuthenticatorName = customLocalAuthenticatorName;
	}

	public String getCustomLocalAuthenticatorName(){
		return this.customLocalAuthenticatorName;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("ConfigsDTO {");
		sb.append("\n\tisEnabled = ").append(isEnabled)
				.append(",\n\tshowFailureReason = ").append(showFailureReason)
				.append(",\n\tmfaTokenValidityPeriod = ").append(mfaTokenValidityPeriod)
				.append(",\n\ttimestampSkew = ").append(timestampSkew)
				.append(",\n\tcustomLocalAuthenticatorName = ").append(timestampSkew)
				.append("\n}");
		return sb.toString();

	}
}
