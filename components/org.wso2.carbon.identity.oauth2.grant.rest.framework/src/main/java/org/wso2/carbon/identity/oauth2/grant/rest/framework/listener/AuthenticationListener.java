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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.listener;

import org.wso2.carbon.identity.oauth2.grant.rest.framework.context.RestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;

/**
 * This allows an extension point to implement various additional operations before and after
 * user/otp authentication is done.
 */
public interface AuthenticationListener {

	/**
	 * Get the execution order identifier for this listener.
	 *
	 * @return The execution order identifier integer value.
	 */
	int getExecutionOrderId();

	/**
	 * Define any additional actions before actual authentication happens.
	 *
	 * @param authContext         AuthenticationContext object of the user.
	 * @return whether the listener execution is successful.
	 * @throws AuthenticationException if any server or client error occurred.
	 */
	boolean doPreAuthenticate(RestAuthenticationContext authContext) throws AuthenticationException;

	/**
	 * Define any additional actions after actual authentication happens.
	 *
	 * @param authContext         User object of the user.
	 * @return whether the listener execution is successful.
	 * @throws AuthenticationException if any server or client error occurred.
	 */
	boolean doPostAuthenticate(RestAuthenticationContext authContext) throws AuthenticationException;

}
