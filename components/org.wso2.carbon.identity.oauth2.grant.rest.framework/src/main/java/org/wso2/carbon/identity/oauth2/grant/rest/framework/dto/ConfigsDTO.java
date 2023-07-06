/*
 *  Copyright (c) 2023, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC licenses this file to you under the Apache license,
 *  Version 2.0 (the "license"); you may not use this file except
 *  in compliance with the license.
 *  You may obtain a copy of the license at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class holds the SMS OTP feature configurations.
 */
public class ConfigsDTO {

	private static final Log LOG = LogFactory.getLog(ConfigsDTO.class);
	private boolean isEnabled;
	private boolean showFailureReason;
	private int flowIdValidityPeriod;
	private int timestampSkew;


	private String clientMandatoryParamsEmpty;
	private String clientInvalidAuthenticator;
	private String clientInvalidClientId;
	private String clientIncorrectUserCredentials;
	private String clientInactiveFlowId;
	private String clientInvalidFlowId;
	private String clientAuthStepOutOfBound;
	private String clientUnSupportedAuthenticator;
	private String clientExpiredFlowId;
	private String clientLockedUserAccount;
	private String clientDisabledUserAccount;
	private String clientFlowIdMismatch;
	private String clientInvalidUserCredentials;
	private String clientCrossTenantAccessRestriction;
	private String clientUsernameResolveFailed;

	public boolean isEnabled() {
		return isEnabled;
	}

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


	public String getClientMandatoryParamsEmpty() {
		return clientMandatoryParamsEmpty;
	}

	public void setClientMandatoryParamsEmpty(String clientMandatoryParamsEmpty) {
		this.clientMandatoryParamsEmpty = clientMandatoryParamsEmpty;
	}

	public String getClientInvalidAuthenticator() {
		return clientInvalidAuthenticator;
	}

	public void setClientInvalidAuthenticator(String clientInvalidAuthenticator) {
		this.clientInvalidAuthenticator = clientInvalidAuthenticator;
	}

	public String getClientInvalidClientId() {
		return clientInvalidClientId;
	}

	public void setClientInvalidClientId(String clientInvalidClientId) {
		this.clientInvalidClientId = clientInvalidClientId;
	}

	public String getClientIncorrectUserCredentials() {
		return clientIncorrectUserCredentials;
	}

	public void setClientIncorrectUserCredentials(String clientIncorrectUserCredentials) {
		this.clientIncorrectUserCredentials = clientIncorrectUserCredentials;
	}

	public String getClientInactiveFlowId() {
		return clientInactiveFlowId;
	}

	public void setClientInactiveFlowId(String clientInactiveFlowId) {
		this.clientInactiveFlowId = clientInactiveFlowId;
	}

	public String getClientInvalidFlowId() {
		return clientInvalidFlowId;
	}

	public void setClientInvalidFlowId(String clientInvalidFlowId) {
		this.clientInvalidFlowId = clientInvalidFlowId;
	}

	public String getClientAuthStepOutOfBound() {
		return clientAuthStepOutOfBound;
	}

	public void setClientAuthStepOutOfBound(String clientAuthStepOutOfBound) {
		this.clientAuthStepOutOfBound = clientAuthStepOutOfBound;
	}

	public String getClientUnSupportedAuthenticator() {
		return clientUnSupportedAuthenticator;
	}

	public void setClientUnSupportedAuthenticator(String clientUnSupportedAuthenticator) {
		this.clientUnSupportedAuthenticator = clientUnSupportedAuthenticator;
	}

	public String getClientExpiredFlowId() {
		return clientExpiredFlowId;
	}

	public void setClientExpiredFlowId(String clientExpiredFlowId) {
		this.clientExpiredFlowId = clientExpiredFlowId;
	}

	public String getClientLockedUserAccount() {
		return clientLockedUserAccount;
	}

	public void setClientLockedUserAccount(String clientLockedUserAccount) {
		this.clientLockedUserAccount = clientLockedUserAccount;
	}

	public String getClientDisabledUserAccount() {
		return clientDisabledUserAccount;
	}

	public void setClientDisabledUserAccount(String clientDisabledUserAccount) {
		this.clientDisabledUserAccount = clientDisabledUserAccount;
	}

	public String getClientFlowIdMismatch() {
		return clientFlowIdMismatch;
	}

	public void setClientFlowIdMismatch(String clientFlowIdMismatch) {
		this.clientFlowIdMismatch = clientFlowIdMismatch;
	}

	public String getClientInvalidUserCredentials() {
		return clientInvalidUserCredentials;
	}

	public void setClientInvalidUserCredentials(String clientInvalidUserCredentials) {
		this.clientInvalidUserCredentials = clientInvalidUserCredentials;
	}

	public String getClientCrossTenantAccessRestriction() {
		return clientCrossTenantAccessRestriction;
	}

	public void setClientCrossTenantAccessRestriction(String clientCrossTenantAccessRestriction) {
		this.clientCrossTenantAccessRestriction = clientCrossTenantAccessRestriction;
	}

	public String getClientUsernameResolveFailed() {
		return clientUsernameResolveFailed;
	}

	public void setClientUsernameResolveFailed(String clientUsernameResolveFailed) {
		this.clientUsernameResolveFailed = clientUsernameResolveFailed;
	}

	@Override
	public String toString() {
		return "ConfigsDTO{" +
				"isEnabled=" + isEnabled +
				", showFailureReason=" + showFailureReason +
				", flowIdValidityPeriod=" + flowIdValidityPeriod +
				", timestampSkew=" + timestampSkew +
				", clientMandatoryParamsEmpty='" + clientMandatoryParamsEmpty + '\'' +
				", clientInvalidAuthenticator='" + clientInvalidAuthenticator + '\'' +
				", clientInvalidClientId='" + clientInvalidClientId + '\'' +
				", clientIncorrectUserCredentials='" + clientIncorrectUserCredentials + '\'' +
				", clientInactiveFlowId='" + clientInactiveFlowId + '\'' +
				", clientInvalidFlowId='" + clientInvalidFlowId + '\'' +
				", clientAuthStepOutOfBound='" + clientAuthStepOutOfBound + '\'' +
				", clientUnSupportedAuthenticator='" + clientUnSupportedAuthenticator + '\'' +
				", clientExpiredFlowId='" + clientExpiredFlowId + '\'' +
				", clientLockedUserAccount='" + clientLockedUserAccount + '\'' +
				", clientDisabledUserAccount='" + clientDisabledUserAccount + '\'' +
				", clientFlowIdMismatch='" + clientFlowIdMismatch + '\'' +
				", clientInvalidUserCredentials='" + clientInvalidUserCredentials + '\'' +
				", clientCrossTenantAccessRestriction='" + clientCrossTenantAccessRestriction + '\'' +
				", clientUsernameResolveFailed='" + clientUsernameResolveFailed + '\'' +
				'}';
	}
}
