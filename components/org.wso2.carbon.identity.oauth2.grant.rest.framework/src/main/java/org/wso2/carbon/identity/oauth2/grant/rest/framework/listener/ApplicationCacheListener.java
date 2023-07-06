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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.listener.AbstractApplicationMgtListener;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.cache.AuthCache;

/**
 * This class is used to keep cache data when multi menancy is used.
 */
public class ApplicationCacheListener extends AbstractApplicationMgtListener {

	private static final Log LOG = LogFactory.getLog(ApplicationCacheListener.class);
	private static AuthCache authCache = null;

	public ApplicationCacheListener() {

		authCache = AuthCache.getInstance();
	}

	@Override
	public int getDefaultOrderId() {

		return 0;
	}

	@Override
	public boolean doPostUpdateApplication(ServiceProvider serviceProvider, String tenantDomain, String userName)
			throws IdentityApplicationManagementException {

		authCache.clear(tenantDomain);
		return true;
	}

	@Override
	public boolean doPostDeleteApplication(ServiceProvider serviceProvider, String tenantDomain, String userName)
			throws IdentityApplicationManagementException {

		authCache.clear(tenantDomain);
		return true;
	}

}
