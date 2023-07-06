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
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.application.mgt.listener.ApplicationMgtListener;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.AuthenticationListenerService;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.RestAuthenticationService;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.RestAuthenticationServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.listener.ApplicationCacheListener;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.util.RestAuthUtil;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.user.core.service.RealmService;


/**
 * OSGI service component of the Rest Authentication service.
 */
@Component(name = "org.wso2.carbon.identity.oauth2.grant.rest", immediate = true)
public class AuthenticationServiceComponent {

	private static final Log LOG = LogFactory.getLog(AuthenticationServiceComponent.class);

	@Activate
	protected void activate(ComponentContext componentContext) {

		try {
			RestAuthUtil.readConfigurations();
			boolean isEnabled = AuthenticationServiceDataHolder.getConfigs().isEnabled();

			if (isEnabled) {
				BundleContext bundleContext = componentContext.getBundleContext();
				bundleContext.registerService(RestAuthenticationService.class.getName(),
						new RestAuthenticationServiceImpl(), null);
				LOG.info("Authentication Service component activated successfully.");
				bundleContext.registerService(ApplicationMgtListener.class.getName(),
						new ApplicationCacheListener(), null);
				LOG.info("Application Management Listener Service component activated successfully.");
			} else {
				LOG.error("Authentication Service is not enabled.");
			}
		} catch (Throwable e) {
			LOG.error("Error while activating Authentication Service.", e);
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Authentication Service component is deactivated");
		}
	}

	/**
	 * Realm Service.
	 */
	@Reference
			(name = "realm.service",
			service = RealmService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetRealmService")
	protected void setRealmService(RealmService realmService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting the Realm Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setRealmService(realmService);
	}

	protected void unsetRealmService(RealmService realmService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the Realm Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setRealmService(null);
	}

	/**
	 * SMS OTP Service.
	 */
	@Reference
			(name = "smsotp.service",
			service = SMSOTPService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetSMSOtpService")
	protected void setSMSOtpService(SMSOTPService smsOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting the SMSOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setSMSOtpService(smsOtpService);

	}

	protected void unsetSMSOtpService(SMSOTPService smsOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the SMSOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setSMSOtpService(null);

	}

	/**
	 * Email OTP Service.
	 */
	@Reference
			(name = "emailotp.service",
			service = EmailOtpService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetEmailOtpService")
	protected void setEmailOtpService(EmailOtpService emailOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting the EmailOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setEmailOtpService(emailOtpService);

	}

	protected void unsetEmailOtpService(EmailOtpService emailOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the EmailOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setEmailOtpService(null);

	}

	/**
	 * Rest Authentication Listener Service.
	 */
	@Reference
			(name = "rest.auth.listener.service",
			service = AuthenticationListenerService.class,
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetAuthListenerService")
	protected void setAuthListenerService(AuthenticationListenerService authListenerService)
			throws AuthenticationException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting Authentication Listener Service.");
		}

		AuthenticationServiceDataHolder.getInstance().setAuthListenerService(authListenerService);
	}

	protected void unsetAuthListenerService(AuthenticationListenerService authListenerService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset Authentication Listener Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setAuthListenerService(null);
	}

	/**
	 * Multi Attribute Login Service.
	 */
	@Reference(
			name = "MultiAttributeLoginService",
			service = MultiAttributeLoginService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetMultiAttributeLoginService")
	protected void setMultiAttributeLoginService(MultiAttributeLoginService multiAttributeLogin) {

		AuthenticationServiceDataHolder.getInstance().setMultiAttributeLogin(multiAttributeLogin);
	}

	protected void unsetMultiAttributeLoginService(MultiAttributeLoginService multiAttributeLogin) {

		AuthenticationServiceDataHolder.getInstance().setMultiAttributeLogin(null);
	}

}
