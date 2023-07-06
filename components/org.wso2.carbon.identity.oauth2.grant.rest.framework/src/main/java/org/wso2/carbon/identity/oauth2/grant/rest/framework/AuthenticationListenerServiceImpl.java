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

package org.wso2.carbon.identity.oauth2.grant.rest.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.listener.AuthenticationListener;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is used to execute extensible listeners developed for this connector.
 */
public class AuthenticationListenerServiceImpl implements AuthenticationListenerService {

	private static final Log LOG = LogFactory.getLog(AuthenticationListenerServiceImpl.class);
	private static Map<Integer, AuthenticationListener> authenticationListeners;
	private static Collection<AuthenticationListener> authenticationListenerCollection;

	@Override
	public synchronized void addAuthenticationListener (AuthenticationListener authenticationListenerService)
			throws AuthenticationException {

		authenticationListenerCollection = null;

		if (authenticationListeners == null) {
			authenticationListeners = new TreeMap<Integer, AuthenticationListener>();
		}
		authenticationListeners.put(authenticationListenerService.getExecutionOrderId(),
				authenticationListenerService);
	}

	@Override
	public synchronized void removeAuthenticationListener(AuthenticationListener authenticationListenerService)
			throws AuthenticationException {

		if (authenticationListenerService != null &&
				authenticationListeners != null) {
			authenticationListeners.remove(authenticationListenerService.getExecutionOrderId());
			authenticationListenerCollection = null;

		}
	}

	protected static synchronized Collection<AuthenticationListener> getAuthenticationListeners() {

		if (authenticationListeners == null) {
			authenticationListeners = new TreeMap<Integer, AuthenticationListener>();
		}
		if (authenticationListenerCollection == null) {
			authenticationListenerCollection =
					authenticationListeners.values();
		}
		return authenticationListenerCollection;
	}
}
