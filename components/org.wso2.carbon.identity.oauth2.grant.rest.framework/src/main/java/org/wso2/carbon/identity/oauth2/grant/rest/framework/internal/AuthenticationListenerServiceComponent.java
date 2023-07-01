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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.AuthenticationListenerService;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.AuthenticationListenerServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;

@Component(name = "org.wso2.carbon.identity.oauth2.grant.rest.auth.listener.service", immediate = true)
public class AuthenticationListenerServiceComponent {

	private static final Log log = LogFactory.getLog(AuthenticationServiceComponent.class);

	@Activate
	protected void activate(ComponentContext componentContext) throws AuthenticationException {

		BundleContext bundleContext = componentContext.getBundleContext();
		bundleContext.registerService(AuthenticationListenerService.class.getName(),
				new AuthenticationListenerServiceImpl(), null);

		log.info("Authentication Listener Service component activated successfully.");

	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		if (log.isDebugEnabled()) {
			log.debug("Authentication Listener Service component is deactivated");
		}
	}

}
