package org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener;

import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

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
	 * Define any additional actions before actual authentication happens
	 *
	 * @param authContext         AuthenticationContext object of the user.
	 * @return whether the listener execution is successful.
	 * @throws MFAAuthException if any server or client error occurred.
	 */
	public boolean doPreAuthenticate(AuthenticationContext authContext) throws MFAAuthException;

	/**
	 * Define any additional actions after actual authentication happens
	 *
	 * @param authContext         User object of the user.
	 * @return whether the listener execution is successful.
	 * @throws MFAAuthException if any server or client error occurred.
	 */
	public boolean doPostAuthenticate(AuthenticationContext authContext) throws MFAAuthException;

}
