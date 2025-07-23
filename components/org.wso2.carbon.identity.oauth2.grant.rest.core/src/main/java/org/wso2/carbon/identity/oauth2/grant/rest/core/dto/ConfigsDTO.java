/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.grant.rest.core.dto;

/**
 * DTO class for configurations.
 */
public class ConfigsDTO {

	private boolean isEnabled;
	private boolean showFailureReason;
	private int flowIdValidityPeriod;
	private int timestampSkew;
	private boolean sendNotificationTargetInInitResponse;
	private String emailAddressRegex;
	private String mobileNumberRegex;
	private String emailOtpAuthenticatorAlias;
	private String smsOtpAuthenticatorAlias;

	public void setEnabled(boolean enabled) {

		isEnabled = enabled;
	}

	public boolean isShowFailureReason() {

		return showFailureReason;
	}

	public void setShowFailureReason(boolean showFailureReason) {

		this.showFailureReason = showFailureReason;
	}

	public int getFlowIdValidityPeriod() {

		return flowIdValidityPeriod;
	}

	public void setFlowIdValidityPeriod(int flowIdValidityPeriod) {

		this.flowIdValidityPeriod = flowIdValidityPeriod;
	}

	public int getTimestampSkew() {

		return timestampSkew;
	}

	public void setTimestampSkew (int timestampSkew) {

		this.timestampSkew = timestampSkew;
	}

	public boolean isSendNotificationTargetInInitResponse() {

		return sendNotificationTargetInInitResponse;
	}

	public void setSendNotificationTargetInInitResponse(boolean sendNotificationTargetInInitResponse) {

		this.sendNotificationTargetInInitResponse = sendNotificationTargetInInitResponse;
	}

	public String getEmailAddressRegex() {

		return emailAddressRegex;
	}

	public void setEmailAddressRegex(String emailAddressRegex) {

		this.emailAddressRegex = emailAddressRegex;
	}

	public String getMobileNumberRegex() {

		return mobileNumberRegex;
	}

	public void setMobileNumberRegex(String mobileNumberRegex) {

		this.mobileNumberRegex = mobileNumberRegex;
	}

	public String getEmailOtpAuthenticatorAlias() {

		return emailOtpAuthenticatorAlias;
	}

	public void setEmailOtpAuthenticatorAlias(String emailOtpAuthenticatorAlias) {

		this.emailOtpAuthenticatorAlias = emailOtpAuthenticatorAlias;
	}

	public String getSmsOtpAuthenticatorAlias() {

		return smsOtpAuthenticatorAlias;
	}

	public void setSmsOtpAuthenticatorAlias(String smsOtpAuthenticatorAlias) {

		this.smsOtpAuthenticatorAlias = smsOtpAuthenticatorAlias;
	}

	@Override
	public String toString() {
		return "ConfigsDTO{" +
				"isEnabled=" + isEnabled +
				", showFailureReason=" + showFailureReason +
				", flowIdValidityPeriod=" + flowIdValidityPeriod +
				", timestampSkew=" + timestampSkew +
				", sendNotificationTargetInInitResponse=" + sendNotificationTargetInInitResponse +
				", emailAddressRegex='" + emailAddressRegex +
				", mobileNumberRegex='" + mobileNumberRegex +
				", emailOtpAuthenticatorAlias='" + emailOtpAuthenticatorAlias +
				", smsOtpAuthenticatorAlias='" + smsOtpAuthenticatorAlias +
				'}';
	}
}
