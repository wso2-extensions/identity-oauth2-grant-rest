package org.wso2.carbon.identity.oauth2.grant.mfa.framework;


import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AuthenticationListener;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class MFAAuthListenerServiceImpl implements MFAAuthListenerService {

	private static Map<Integer, AuthenticationListener> authenticationListeners;
	private static Collection<AuthenticationListener> authenticationListenerCollection;

	@Override
	public synchronized void addAuthenticationListener (AuthenticationListener authenticationListenerService)
			throws MFAAuthException {

		authenticationListenerCollection = null;

		if (authenticationListeners == null) {
			authenticationListeners = new TreeMap<Integer, AuthenticationListener>();
		}
		authenticationListeners.put(authenticationListenerService.getExecutionOrderId(),
				authenticationListenerService);

	}

	@Override
	public synchronized void removeAuthenticationListener(AuthenticationListener authenticationListenerService) throws MFAAuthException {

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
