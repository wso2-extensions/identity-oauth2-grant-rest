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

package org.wso2.carbon.identity.oauth2.grant.rest.core.internal;

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
import org.wso2.carbon.identity.event.IdentityEventConfigBuilder;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.identity.event.services.IdentityEventServiceImpl;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.AuthenticationListenerService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.RestAuthenticationService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.RestAuthenticationServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.listener.ApplicationCacheListener;
import org.wso2.carbon.identity.oauth2.grant.rest.core.util.RestAuthUtil;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.user.core.service.RealmService;
import java.util.ArrayList;
import java.util.List;

/**
 * OSGI service component of the Rest Authentication service.
 */
@Component(name = "org.wso2.carbon.identity.oauth2.grant.rest", immediate = true)
public class AuthenticationServiceComponent {

	private static final Log LOG = LogFactory.getLog(AuthenticationServiceComponent.class);

	public static List<AbstractEventHandler> eventHandlerList = new ArrayList<>();
	@Activate
	protected void activate(ComponentContext componentContext) {

		try {
			RestAuthUtil.readConfigurations();
			BundleContext bundleContext = componentContext.getBundleContext();
			bundleContext.registerService(RestAuthenticationService.class.getName(),
					new RestAuthenticationServiceImpl(), null);
			LOG.info("Authentication Service component activated successfully.");
			bundleContext.registerService(ApplicationMgtListener.class.getName(),
					new ApplicationCacheListener(), null);
			LOG.info("Application Management Listener Service component activated successfully.");

			AuthenticationServiceDataHolder.getInstance().setIdentityEventService
					(new IdentityEventServiceImpl(eventHandlerList, Integer.parseInt(IdentityEventConfigBuilder
							.getInstance().getThreadPoolSize())));

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
	 * @param realmService 	A RealmService object.
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

	/**
	 * @param realmService 	A RealmService object.
	 */
	protected void unsetRealmService(RealmService realmService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the Realm Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setRealmService(null);
	}

	/**
	 * @param smsOtpService 	A SMSOTPService object.
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

	/**
	 * @param smsOtpService 	A SMSOTPService object.
	 */
	protected void unsetSMSOtpService(SMSOTPService smsOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the SMSOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setSMSOtpService(null);

	}

	/**
	 * @param emailOtpService 	An EmailOTPService object.
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

	/**
	 * @param emailOtpService 	An EmailOTPService object.
	 */
	protected void unsetEmailOtpService(EmailOtpService emailOtpService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset the EmailOTP Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setEmailOtpService(null);

	}

	/**
	 * @param authListenerService 	An AuthenticationListenerService object.
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

	/**
	 * @param authListenerService 	An AuthenticationListenerService object.
	 */
	protected void unsetAuthListenerService(AuthenticationListenerService authListenerService) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Unset Authentication Listener Service.");
		}
		AuthenticationServiceDataHolder.getInstance().setAuthListenerService(null);
	}

	/**
	 * @param multiAttributeLogin 	A MultiAttributeLoginService object.
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

	/**
	 * @param multiAttributeLogin 	A MultiAttributeLoginService object.
	 */
	protected void unsetMultiAttributeLoginService(MultiAttributeLoginService multiAttributeLogin) {

		AuthenticationServiceDataHolder.getInstance().setMultiAttributeLogin(null);
	}

	/**
	 * @param eventService 	An IdentityEventService object.
	 */
	@Reference(
			name = "EventMgtService",
			service = org.wso2.carbon.identity.event.services.IdentityEventService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetIdentityEventService")
	protected void setIdentityEventService(IdentityEventService eventService) {

		AuthenticationServiceDataHolder.getInstance().setIdentityEventService(eventService);
	}

	/**
	 * @param eventService 	An IdentityEventService object.
	 */
	protected void unsetIdentityEventService(IdentityEventService eventService) {

		AuthenticationServiceDataHolder.getInstance().setIdentityEventService(null);
	}

}
