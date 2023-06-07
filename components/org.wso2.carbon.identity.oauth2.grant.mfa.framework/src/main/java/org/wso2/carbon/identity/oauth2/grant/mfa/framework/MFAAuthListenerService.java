package org.wso2.carbon.identity.oauth2.grant.mfa.framework;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener.AuthenticationListener;

public interface MFAAuthListenerService {

	/**
	 * This method adds a Listener class to the Listener collection.
	 *
	 * @param authenticationListenerService        Authentication Listener Service.
	 * @throws MFAAuthException if any server or client error occurred.
	 */
	void addAuthenticationListener(AuthenticationListener authenticationListenerService)
			throws MFAAuthException;

	/**
	 * This method removes a Listener class from the Listener collection.
	 *
	 * @param authenticationListenerService        Authentication Listener Service.
	 * @throws MFAAuthException Thrown if any server or client error occurred.
	 */
	void removeAuthenticationListener(AuthenticationListener authenticationListenerService)
			throws MFAAuthException;

}
