package org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthListenerService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthListenerServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

@Component(name = "com.mfa.auth.listener.service", immediate = true)
public class AuthListenerServiceComponent {

	private static final Log log = LogFactory.getLog(MFAAuthServiceComponent.class);

	@Activate
	protected void activate(ComponentContext componentContext) throws MFAAuthException {

		BundleContext bundleContext = componentContext.getBundleContext();
		bundleContext.registerService(MFAAuthListenerService.class.getName(), new MFAAuthListenerServiceImpl(), null);

		log.info("MFA Authentication Listener Service component activated successfully.");

	}

	@Deactivate
	protected void deactivate(ComponentContext componentContext) {
		if (log.isDebugEnabled()) {
			log.debug("MFA Authentication Listener Service component is deactivated");
		}
	}

}
