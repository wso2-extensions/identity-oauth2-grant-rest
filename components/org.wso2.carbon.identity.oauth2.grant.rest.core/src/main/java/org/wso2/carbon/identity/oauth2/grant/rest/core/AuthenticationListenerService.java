/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.oauth2.grant.rest.core;

import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.listener.AuthenticationListener;

/**
 * Interface for exetenssion points.
 */
public interface AuthenticationListenerService {

	/**
	 * This method adds a Listener class to the Listener collection.
	 *
	 * @param authenticationListenerService      Authentication Listener Service.
	 * @throws AuthenticationException 			 If any server or client error occurred.
	 */
	void addAuthenticationListener(AuthenticationListener authenticationListenerService)
			throws AuthenticationException;

	/**
	 * This method removes a Listener class from the Listener collection.
	 *
	 * @param authenticationListenerService     Authentication Listener Service.
	 * @throws AuthenticationException 			Thrown if any server or client error occurred.
	 */
	void removeAuthenticationListener(AuthenticationListener authenticationListenerService)
			throws AuthenticationException;

}
