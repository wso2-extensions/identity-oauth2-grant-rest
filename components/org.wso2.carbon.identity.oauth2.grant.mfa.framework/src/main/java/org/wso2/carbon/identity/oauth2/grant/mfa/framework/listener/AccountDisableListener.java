package org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener;

import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.util.Utils;

public class AccountDisableListener extends AbstractAuthenticationListener {

	@Override
	public int getExecutionOrderId() {
		return 14;
	}

	@Override
	public boolean doPreAuthenticate(AuthenticationContext authContext) throws MFAAuthException {
		return true;
	}

	@Override
	public boolean doPostAuthenticate(AuthenticationContext authContext) throws MFAAuthException {

		// check whether user account is disabled
		try {
			if (Utils.isAccountDisabled(authContext.getUser())) {
				throw Util.handleClientException(
						Constants.ErrorMessage.CLIENT_DISABLED_ACCOUNT, authContext.getUser().toFullQualifiedUsername());
			}
		} catch(IdentityRecoveryException e){
			throw Util.handleServerException(Constants.ErrorMessage.SERVER_ACCOUNT_STATUS_ERROR,
					String.format("Error while checking the account status for the user : %s.",
							authContext.getUser().toFullQualifiedUsername()), e);
		}
		return true;

	}
}
