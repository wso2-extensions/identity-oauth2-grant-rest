/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.AuthenticationListenerService;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.ConfigsDTO;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Data holder for Rest Authentication Service.
 */
public class AuthenticationServiceDataHolder {

	private static final AuthenticationServiceDataHolder dataHolder = new AuthenticationServiceDataHolder();
	private RealmService realmService;
	private SMSOTPService smsOtpService;
	private EmailOtpService emailOtpService;
	private AuthenticationListenerService authListenerService;
	private MultiAttributeLoginService multiAttributeLoginService;
	private IdentityEventService identityEventService;
	private static final ConfigsDTO configs = new ConfigsDTO();

	public static AuthenticationServiceDataHolder getInstance() {

		return dataHolder;
	}

	public RealmService getRealmService() {

		return realmService;
	}

	public void setRealmService(RealmService realmService) {

		this.realmService = realmService;
	}

	public void setSMSOtpService(SMSOTPService smsOtpService) {

		this.smsOtpService = smsOtpService;
	}

	public void setEmailOtpService(EmailOtpService emailOtpService) {

		this.emailOtpService = emailOtpService;
	}

	public void setAuthListenerService(AuthenticationListenerService authListenerService) {

		this.authListenerService = authListenerService;
	}

	public MultiAttributeLoginService getMultiAttributeLogin() {

		return multiAttributeLoginService;
	}

	public void setIdentityEventService(IdentityEventService identityEventService) {

		this.identityEventService = identityEventService;
	}

	public void setMultiAttributeLogin(MultiAttributeLoginService multiAttributeLoginService) {

		this.multiAttributeLoginService = multiAttributeLoginService;
	}

	public static ConfigsDTO getConfigs() {

		return configs;
	}

}
