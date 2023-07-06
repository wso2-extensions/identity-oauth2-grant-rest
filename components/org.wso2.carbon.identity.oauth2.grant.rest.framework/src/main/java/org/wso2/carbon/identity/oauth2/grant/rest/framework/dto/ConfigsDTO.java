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

	@Override
	public String toString() {
		return "ConfigsDTO{" +
				"isEnabled=" + isEnabled +
				", showFailureReason=" + showFailureReason +
				", flowIdValidityPeriod=" + flowIdValidityPeriod +
				", timestampSkew=" + timestampSkew +
				'}';
	}
	
}
