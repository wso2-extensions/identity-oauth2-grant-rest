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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.IdentityProviderProperty;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.event.IdentityEventConfigBuilder;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.bean.ModuleConfiguration;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.context.RestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.internal.AuthenticationServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.util.Util;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PasswordExpiryListener extends AbstractAuthenticationListener {

	private static final Log LOG = LogFactory.getLog(PasswordExpiryListener.class);

	@Override
	public int getExecutionOrderId() {
		return 12;
	}

	@Override
	public boolean doPreAuthenticate (RestAuthenticationContext authContext) throws AuthenticationException {
		String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(authContext.getUser().getUserName());

		// check whether user password is expired
		if (isUserPasswordExpired(tenantAwareUserName, authContext.getUser().getTenantDomain())) {
			throw Util.handleClientException(
					Constants.ErrorMessage.CLIENT_EXPIRED_USER_PASSWORD, authContext.getUser().
							toFullQualifiedUsername());
		}
		return true;
	}

	@Override
	public boolean doPostAuthenticate(RestAuthenticationContext authContext) {
		return true;
	}

	/**
	 * Checks if the password has expired.
	 *
	 * @param tenantAwareUsername The tenant aware username of the user trying to authenticate.
	 * @param tenantDomain        The tenant domain of the user trying to authenticate.
	 * @return True if the password has expired.
	 * @throws AuthenticationException if the authentication failed for the user trying to login.
	 */
	public static boolean isUserPasswordExpired(String tenantAwareUsername, String tenantDomain)
			throws AuthenticationException {

		UserStoreManager userStoreManager;
		int userTenantId = IdentityTenantUtil.getTenantId(tenantDomain);

		try {
			userStoreManager = (AbstractUserStoreManager) AuthenticationServiceDataHolder.getInstance()
					.getRealmService().getTenantUserRealm(userTenantId).getUserStoreManager();
		} catch (UserStoreException e) {
			throw Util.handleServerException(Constants.ErrorMessage.SERVER_USER_STORE_MANAGER_ERROR,
					"Error while retrieving user store manager.", e);
		}

		String passwordLastChangedTime;
		String lastCredentialUpdateClaimURI = Constants.LAST_CREDENTIAL_UPDATE_TIMESTAMP_CLAIM;
		String createdClaimURI = Constants.CREATED_CLAIM;
		try {
			// trying to first get a value for the 'lastPasswordUpdateTime' claim and if that fails the fallback is
			// the 'created' claim
			passwordLastChangedTime = getClaimValue(userStoreManager, lastCredentialUpdateClaimURI, tenantAwareUsername)
					.orElse(convertCreatedDateToEpochString(
							getClaimValue(userStoreManager, createdClaimURI, tenantAwareUsername).get()));
		} catch (UserStoreException | ParseException e) {
			throw new AuthenticationException("Error occurred while loading user claim", e);
		}

		long passwordChangedTime;
		passwordChangedTime = Long.parseLong(passwordLastChangedTime);

		int daysDifference = 0;
		long currentTimeMillis = System.currentTimeMillis();
		if (passwordChangedTime > 0) {
			Calendar currentTime = Calendar.getInstance();
			currentTime.add(Calendar.DATE, (int) currentTime.getTimeInMillis());
			daysDifference = (int) TimeUnit.MILLISECONDS.toDays(currentTimeMillis - passwordChangedTime);
		}

		int passwordExpiryInDays = Constants.PASSWORD_EXPIRY_IN_DAYS_DEFAULT_VALUE;

		// Getting the configured number of days before password expiry in days
		String passwordExpiryInDaysConfiguredValue = getResidentIdpProperty(tenantDomain,
				Constants.PASSWORD_EXPIRY_IN_DAYS_FROM_CONFIG)
				.orElseGet(() -> getIdentityEventProperty(
						Constants.PASSWORD_EXPIRY_IN_DAYS_FROM_CONFIG).get());

		if (StringUtils.isNotBlank(passwordExpiryInDaysConfiguredValue)) {
			passwordExpiryInDays = Integer.parseInt(passwordExpiryInDaysConfiguredValue);
		}

		return daysDifference > passwordExpiryInDays;
	}

	private static Optional<String> getClaimValue
			(UserStoreManager userStoreManager, String claimURI, String tenantAwareUsername) throws UserStoreException {

		String[] claimURIs = new String[]{claimURI};
		Map<String, String> claimValueMap =
				userStoreManager.getUserClaimValues(tenantAwareUsername, claimURIs, null);
		if (claimValueMap != null && !claimValueMap.isEmpty()) {
			return Optional.of(claimValueMap.get(claimURI));
		}
		return Optional.empty();
	}

	private static String convertCreatedDateToEpochString(String createdDate) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.CREATED_CLAIM_DATE_FORMAT);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Constants.CREATED_CLAIM_TIMEZONE));

		return String.valueOf(simpleDateFormat.parse(createdDate).getTime());
	}

	/**
	 * Get the identity property specified in identity-event.properties.
	 *
	 * @param propertyName The name of the property which should be fetched.
	 * @return The required property.
	 */
	public static Optional<String> getIdentityEventProperty(String propertyName) {

		// Retrieving properties set in identity event properties
		Optional<String> propertyValue = Optional.empty();
		try {
			ModuleConfiguration moduleConfiguration = IdentityEventConfigBuilder.getInstance()
					.getModuleConfigurations(Constants.PASSWORD_CHANGE_EVENT_HANDLER_NAME);

			if (moduleConfiguration != null && moduleConfiguration.getModuleProperties() != null) {
				propertyValue = Optional.of(moduleConfiguration.getModuleProperties().getProperty(propertyName));
			}
		} catch (IdentityEventException e) {
			LOG.warn("An error occurred while retrieving module properties");
			if (LOG.isDebugEnabled()) {
				LOG.debug("An error occurred while retrieving module properties because " + e.getMessage(), e);
			}
		}
		return propertyValue;
	}

	/**
	 * Retrieve the password expiry property from resident IdP.
	 *
	 * @param tenantDomain tenant domain which user belongs to.
	 * @param propertyName name of the property to be retrieved.
	 * @return the value of the requested property.
	 * @throws AuthenticationException if retrieving property from resident idp fails.
	 */
	public static Optional<String> getResidentIdpProperty(String tenantDomain, String propertyName)
			throws AuthenticationException {

		IdentityProvider residentIdP;
		try {
			residentIdP = IdentityProviderManager.getInstance().getResidentIdP(tenantDomain);

			if (residentIdP == null) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Resident IdP is not found for tenant: " + tenantDomain);
				}
				return Optional.empty();
			}
		} catch (IdentityProviderManagementException e) {
			throw new AuthenticationException("Error occurred while retrieving the resident IdP for tenant: " +
					tenantDomain, e);
		}

		IdentityProviderProperty property = IdentityApplicationManagementUtil
				.getProperty(residentIdP.getIdpProperties(), propertyName);

		String propertyValue = null;
		if (property != null) {
			propertyValue = property.getValue();
		}
		return Optional.ofNullable(propertyValue);
	}
}