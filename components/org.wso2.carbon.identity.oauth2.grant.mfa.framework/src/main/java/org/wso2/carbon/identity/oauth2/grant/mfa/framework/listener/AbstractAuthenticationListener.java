package org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener;

import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

public class AbstractAuthenticationListener implements AuthenticationListener {

	@Override
	public int getExecutionOrderId() {
		return 0;
	}

	@Override
	public boolean doPreAuthenticate(AuthenticationContext authContext) throws MFAAuthException {
		return true;
	}

	@Override
	public boolean doPostAuthenticate(AuthenticationContext authContext) throws MFAAuthException {
		return true;
	}
}
