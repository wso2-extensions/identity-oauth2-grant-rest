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

import org.apache.commons.lang3.StringUtils;
import org.wso2.carbon.identity.smsotp.common.constant.Constants;

/**
 * This class represents the model of the OTP validation
 * failure reason message, if any.
 */
public class AuthenticationFailureReasonDTO {

	String code;
	String message;
	String description;
	int remainingAttempts;

	public AuthenticationFailureReasonDTO(String code, String message, String description) {

		this.code = code;
		this.message = message;
		this.description = description;
		this.remainingAttempts = 0;
	}

	public AuthenticationFailureReasonDTO
			(String code, String message, String description, int remainingAttempts) {

		this.code = code;
		this.message = message;
		this.description = description;
		this.remainingAttempts = remainingAttempts;
	}

	public AuthenticationFailureReasonDTO(Constants.ErrorMessage error, String data) {

		this.code = error.getCode();
		this.message = error.getMessage();
		description = StringUtils.isNotBlank(data) ? String.format(error.getDescription(), data)
				: error.getDescription();
		this.remainingAttempts = 0;
	}

	/**
	 * This method composes the error message for failed authentication validation attempts.
	 *
	 * @param error                     Error message.
	 * @param data                      Data passed for the string argument in error message.
	 * @param remainingFailedAttempts   No of remaining validation attempts.
	 */
	public AuthenticationFailureReasonDTO
	(Constants.ErrorMessage error, String data, int remainingFailedAttempts) {

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
