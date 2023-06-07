package org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.application.mgt.listener.ApplicationMgtListener;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthListenerService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AccountDisableListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AccountLockListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.ApplicationCacheListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.PasswordExpiryListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.user.core.service.RealmService;


/**
 * OSGI service component of the MFA Authentication service.
 */
@Component(name = "org.wso2.carbon.identity.oauth2.grant.mfa", immediate = true)
public class MFAAuthServiceComponent {

	private static final Log log = LogFactory.getLog(MFAAuthServiceComponent.class);

	@Activate
	protected void activate(ComponentContext componentContext) {

		try {
			Util.readConfigurations();
			boolean isEnabled = MFAAuthServiceDataHolder.getConfigs().isEnabled();

			if (isEnabled) {
				BundleContext bundleContext = componentContext.getBundleContext();
				bundleContext.registerService(MFAAuthService.class.getName(), new MFAAuthServiceImpl(), null);
				log.debug("MFA Authentication Service component activated successfully.");
				bundleContext.registerService(ApplicationMgtListener.class.getName(), new ApplicationCacheListener(), null);
				log.debug("Application Management Listener Service component activated successfully.");
			} else {
				log.error("MFA Authentication Service is not enabled.");
			}
		} catch (Throwable e) {
			log.error("Error while activating MFA Authentication Service.", e);
		}
	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		if (log.isDebugEnabled()) {
			log.debug("MFA Authentication Service component is deactivated");
		}
	}

	/**
	 * Realm Service.
	 */
	@Reference(name = "realm.service",
			service = RealmService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetRealmService")
	protected void setRealmService(RealmService realmService) {

		if (log.isDebugEnabled()) {
			log.debug("Setting the Realm Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setRealmService(realmService);
	}

	protected void unsetRealmService(RealmService realmService) {

		if (log.isDebugEnabled()) {
			log.debug("Unset the Realm Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setRealmService(null);
	}

	/**
	 * SMS OTP Service.
	 */
	@Reference(name = "smsotp.service",
			service = SMSOTPService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetSMSOtpService")
	protected void setSMSOtpService(SMSOTPService SmsOtpService) {

		if (log.isDebugEnabled()) {
			log.debug("Setting the SMSOTP Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setSMSOtpService(SmsOtpService);

	}

	protected void unsetSMSOtpService(SMSOTPService SmsOtpService) {

		if (log.isDebugEnabled()) {
			log.debug("Unset the SMSOTP Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setSMSOtpService(null);

	}

	/**
	 * Email OTP Service.
	 */
	@Reference(name = "emailotp.service",
			service = EmailOtpService.class,
			cardinality = ReferenceCardinality.MANDATORY,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetEmailOtpService")
	protected void setEmailOtpService(EmailOtpService EmailOtpService) {

		if (log.isDebugEnabled()) {
			log.debug("Setting the EmailOTP Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setEmailOtpService(EmailOtpService);

	}

	protected void unsetEmailOtpService(EmailOtpService EmailOtpService) {

		if (log.isDebugEnabled()) {
			log.debug("Unset the EmailOTP Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setEmailOtpService(null);

	}

	/**
	 * MFA Authentication Listener Service.
	 */
	@Reference(name = "mfa.auth.listener.service",
			service = MFAAuthListenerService.class,
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC,
			unbind = "unsetMFAAuthListenerService")
	protected void setMFAAuthListenerService(MFAAuthListenerService mfaAuthListenerService) throws MFAAuthException {

		if (log.isDebugEnabled()) {
			log.debug("Setting MFA Authentication Listener Service.");
		}

		MFAAuthServiceDataHolder.getInstance().setMFAAuthListenerService(mfaAuthListenerService);

		mfaAuthListenerService.addAuthenticationListener(new PasswordExpiryListener());
		mfaAuthListenerService.addAuthenticationListener(new AccountLockListener());
		mfaAuthListenerService.addAuthenticationListener(new AccountDisableListener());
	}

	protected void unsetMFAAuthListenerService(MFAAuthListenerService mfaAuthListenerService) {

		if (log.isDebugEnabled()) {
			log.debug("Unset MFA Authentication Listener Service.");
		}
		MFAAuthServiceDataHolder.getInstance().setMFAAuthListenerService(null);
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

		MFAAuthServiceDataHolder.getInstance().setMultiAttributeLogin(multiAttributeLogin);
	}

	protected void unsetMultiAttributeLoginService(MultiAttributeLoginService multiAttributeLogin) {

		MFAAuthServiceDataHolder.getInstance().setMultiAttributeLogin(null);
	}

}
