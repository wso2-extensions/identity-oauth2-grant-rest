package org.wso2.carbon.identity.oauth2.grant.mfa.framework.listener;

import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.listener.AbstractApplicationMgtListener;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache.MFAAuthCache;

public class ApplicationCacheListener extends AbstractApplicationMgtListener {

	private static MFAAuthCache mfaAuthCache = null;

	public ApplicationCacheListener(){
		mfaAuthCache = MFAAuthCache.getInstance();
	}

	@Override
	public int getDefaultOrderId() {
		return 0;
	}

	@Override
	public boolean doPostUpdateApplication(ServiceProvider serviceProvider, String tenantDomain, String userName)
			throws IdentityApplicationManagementException {

		mfaAuthCache.clear(tenantDomain);

		return true;
	}

	@Override
	public boolean doPostDeleteApplication(ServiceProvider serviceProvider, String tenantDomain, String userName)
			throws IdentityApplicationManagementException {

		mfaAuthCache.clear(tenantDomain);

		return true;
	}

}
