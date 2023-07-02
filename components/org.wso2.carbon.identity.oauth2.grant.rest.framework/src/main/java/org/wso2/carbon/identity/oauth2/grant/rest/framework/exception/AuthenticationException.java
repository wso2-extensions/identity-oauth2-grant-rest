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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.exception;

import org.wso2.carbon.identity.event.IdentityEventException;

/**
 * This class is to handle Auth Exception.
 */
public class AuthenticationException extends IdentityEventException {

	private String errorCode;
	private String message;
	private String description;

	public AuthenticationException(String message, Throwable throwable) {

		super(message, throwable);
		this.message = message;
	}

	public AuthenticationException(String errorCode, String message) {

		super(errorCode, message);
		this.errorCode = errorCode;
		this.message = message;
	}

	public AuthenticationException(String errorCode, String message, Throwable throwable) {

		super(errorCode, message, throwable);
		this.errorCode = errorCode;
		this.message = message;
	}

	public AuthenticationException(String errorCode, String message, String description) {

		super(errorCode, message);
		this.errorCode = errorCode;
		this.message = message;
		this.description = description;
	}

	public AuthenticationException(String errorCode, String message, String description, Throwable e) {

		super(errorCode, message, e);
		this.errorCode = errorCode;
		this.message = message;
		this.description = description;
	}

	@Override
	public String getErrorCode() {

		return errorCode;
	}

	@Override
	public String getMessage() {

		return message;
	}

	public String getDescription() {

		return description;
	}
}
