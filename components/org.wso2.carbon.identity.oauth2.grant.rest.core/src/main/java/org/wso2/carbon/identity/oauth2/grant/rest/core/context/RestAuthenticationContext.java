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

package org.wso2.carbon.identity.oauth2.grant.rest.core.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.internal.AuthenticationServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.rest.core.util.RestAuthUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Context object for the REST Authentication flow.
 */
public class RestAuthenticationContext {

	private static final Log log = LogFactory.getLog(RestAuthenticationContext.class);
	private String currentAuthenticator;
	private boolean isAuthFlowCompleted;
	private final String userName;
	private String userId;
	private int userTenantId;
	private LinkedHashMap<Integer, String> authenticatedSteps;
	private ServiceProvider serviceProvider;
	private LinkedHashMap<Integer, List<String>> authenticationSteps;
	private final String clientId;
	private int spTenantId;
	private final int flowIdValidityPeriod;
	private String flowId;
	private String flowIdIdentifier;
	private String flowIdState;
	private String newFlodwIdIdentifier;
	private String newFlowId;
	private User user;
	private String password;
	private boolean isValidPassword;
	private String multiAttributeLoginClaimURI;
	private AuthenticatedUser authenticatedUser;
	private  int nextAuthenticationStepId;

	private RestAuthenticationContext(Builder builder) {

		this.currentAuthenticator = builder.authenticator;
		this.isAuthFlowCompleted = builder.isAuthFlowCompleted;
		this.userName = builder.username;
		this.userId = builder.userId;
		this.userTenantId = builder.userTenantId;
		this.authenticatedSteps = builder.authenticatedSteps;
		this.serviceProvider = builder.serviceProvider;
		this.authenticationSteps = builder.authenticationSteps;
		this.clientId = builder.clientId;
		this.spTenantId = builder.spTenantId;
		this.flowIdValidityPeriod = builder.flowIdValidityPeriod;
		this.flowId = builder.flowId;
		this.flowIdIdentifier = builder.flowIdIdentifier;
		this.flowIdState = builder.flowIdState;
		this.newFlodwIdIdentifier = builder.newFlowIdIdentifier;
		this.newFlowId = builder.newFlowId;
		this.password = builder.password;
		this.isValidPassword = builder.isValidPassword;
	}

	public String getCurrentAuthenticator() {
		return currentAuthenticator;
	}

	public boolean isAuthFlowCompleted() {
		return isAuthFlowCompleted;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserId() {
		return userId;
	}

	public int getUserTenantId() {
		return userTenantId;
	}

	public LinkedHashMap<Integer, String> getAuthenticatedSteps() {
		return authenticatedSteps;
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public LinkedHashMap<Integer, List<String>> getAuthenticationSteps() {
		return authenticationSteps;
	}

	public String getClientId() {
		return clientId;
	}

	public int getSpTenantId() {
		return spTenantId;
	}

	public int getFlowIdValidityPeriod() {
		return flowIdValidityPeriod;
	}

	public String getFlowId() {
		return flowId;
	}

	public String getFlowIdIdentifier() {
		return flowIdIdentifier;
	}

	public String getFlowIdState() {
		return flowIdState;
	}

	public String getNewFlowdIdIdentifier() {
		return newFlodwIdIdentifier;
	}

	public String getNewFlowdId() {
		return newFlowId;
	}

	public String getPassword() {
		return password;
	}

	public boolean isValidPassword() {
		return isValidPassword;
	}

	public RestAuthenticationContext setAuthFlowCompleted(boolean isAuthFlowCompleted) {

		this.isAuthFlowCompleted = isAuthFlowCompleted;
		return this;
	}

	public RestAuthenticationContext setUserId (String userId) {

		this.userId = userId;
		return this;
	}

	public RestAuthenticationContext setUserTenantId (int userTenantId) {

		this.userTenantId = userTenantId;
		return this;
	}

	public RestAuthenticationContext setAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {

		this.authenticatedSteps = authenticatedSteps;
		return this;
	}

	public RestAuthenticationContext setServiceProvider (int serviceProviderAppId) throws AuthenticationException {

		this.serviceProvider = RestAuthUtil.getServiceProviderByAppId(serviceProviderAppId);
		setAuthenticationSteps(serviceProvider);
		return this;
	}

	public RestAuthenticationContext setAuthenticationSteps(ServiceProvider serviceProvider) {

		this.authenticationSteps = RestAuthUtil.getAuthStepsForSP(serviceProvider);
		return this;
	}

	public RestAuthenticationContext setSpTenantId (int spTenantId) {

		this.spTenantId = spTenantId;
		return this;
	}

	public RestAuthenticationContext setFlowIdIdentifier (String flowIdIdentifier) {

		this.flowIdIdentifier = flowIdIdentifier;
		return this;
	}

	public RestAuthenticationContext setNewFlowId (String newFlowId) {

		this.newFlowId = newFlowId;
		return this;
	}

	public RestAuthenticationContext setPassword (String password) {

		this.password = password;
		return this;
	}

	public RestAuthenticationContext setValidPassword (boolean isValidPassword) {

		this.isValidPassword = isValidPassword;
		return this;
	}

	public AuthenticatedUser getAuthenticatedUser() {

		return authenticatedUser;
	}

	public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {

		this.authenticatedUser = authenticatedUser;
	}

	/**
	 * This can be used to build AuthContextWrapper Objects.
	 */
	public static class Builder {

		private String authenticator;
		private boolean isAuthFlowCompleted;
		private String username;
		private String userId;
		private int userTenantId;
		private LinkedHashMap<Integer, String> authenticatedSteps = new LinkedHashMap<>();
		private ServiceProvider serviceProvider;
		private LinkedHashMap<Integer, List<String>> authenticationSteps;
		private String clientId;
		private int spTenantId;
		private int flowIdValidityPeriod;
		private String flowId;
		private String flowIdIdentifier;
		private String flowIdState;
		private String newFlowIdIdentifier;
		private String newFlowId;
		private String password;
		private boolean isValidPassword;

		public Builder (HashMap<String, String> params) throws AuthenticationException {
			for (HashMap.Entry<String, String> entry : params.entrySet()) {
				if (entry.getValue() == null) {
					String missingParam = entry.getKey();
					throw RestAuthUtil.handleClientException
							(Constants.ErrorMessage.CLIENT_MANDATORY_VALIDATION_PARAMETERS_EMPTY, missingParam);
				}
				try {
					Method method = this.getClass().getDeclaredMethod(entry.getKey(), String.class);
					method.invoke(this, entry.getValue());
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					throw RestAuthUtil.handleServerException
							(Constants.ErrorMessage.SERVER_REQUEST_PARAM_READING_ERROR,
									String.format("Error while sanitizing the request parameter : %s.", entry.getKey()), e);
				}
			}
		}

		public Builder authenticator (String authenticator) {

			this.authenticator = authenticator;
			return this;
		}

		public Builder setAuthFlowCompleted(boolean isAuthFlowCompleted) {

			this.isAuthFlowCompleted = isAuthFlowCompleted;
			return this;
		}

		public Builder username(String username) {

			this.username = username;
			return this;
		}

		public Builder userId(String userId) {

			this.userId = userId;
			return this;
		}

		public Builder userTenantId(int userTenantId) {

			this.userTenantId = userTenantId;
			return this;
		}

		public Builder authenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {

			this.authenticatedSteps = authenticatedSteps;
			return this;
		}

		public Builder serviceProvider (String clientId) throws AuthenticationException {

			if (clientId != null) {
				this.serviceProvider = RestAuthUtil.getServiceProviderByClientId(clientId);
				authenticationSteps(serviceProvider);
			}
			return this;
		}

		public Builder authenticationSteps (ServiceProvider serviceProvider) {

			this.authenticationSteps = RestAuthUtil.getAuthStepsForSP(serviceProvider);
			return this;
		}

		public Builder clientId(String clientId) {

			this.clientId = clientId;
			return this;
		}

		public Builder spTenantId(int spTenantId) {

			this.spTenantId = spTenantId;
			return this;
		}

		public Builder flowIdValidityPeriod(int flowIdValidityPeriod) {

			this.flowIdValidityPeriod = flowIdValidityPeriod;
			return this;
		}

		public Builder flowId(String flowId) {

			this.flowId = flowId;
			return this;
		}

		public Builder flowIdIdentifier(String flowIdIdentifier) {

			this.flowIdIdentifier = flowIdIdentifier;
			return this;
		}

		public Builder flowIdState(String flowIdState) {

			this.flowIdState = flowIdState;
			return this;
		}

		public Builder newFlowId(String newFlowId) {

			this.newFlowId = newFlowId;
			return this;
		}

		public Builder newFlowIdIdentifier(String newFlowIdIdentifier) {

			this.newFlowIdIdentifier = newFlowIdIdentifier;
			return this;
		}

		public Builder password (String password) {

			this.password = password;
			return this;
		}

		public Builder setValidPassword(boolean isValidPassword) {

			this.isValidPassword = isValidPassword;
			return this;
		}

		private RestAuthenticationContext build() {

			return new RestAuthenticationContext(this);
		}

		static int tenantDomain;
		public RestAuthenticationContext buildForAuthFlowInitialize() throws AuthenticationException {

			return newFlowId(RestAuthUtil.generateUUID())
					.newFlowIdIdentifier(RestAuthUtil.generateUUID())
					.serviceProvider(this.clientId)
					.flowIdState(Constants.FLOW_ID_STATE_ACTIVE)
					.flowIdValidityPeriod(AuthenticationServiceDataHolder.getConfigs().getFlowIdValidityPeriod())
					.spTenantId(IdentityTenantUtil.getTenantId(RestAuthUtil.getTenantDomain()))
					.build();
		}

		public RestAuthenticationContext buildForAuthStepInitialize() {
			return newFlowIdIdentifier(RestAuthUtil.generateUUID())
					.build();
		}

		public RestAuthenticationContext buildForAuthStepValidate() {
			return newFlowIdIdentifier(RestAuthUtil.generateUUID())
					.newFlowId(RestAuthUtil.generateUUID())
					.build();
		}

		public RestAuthenticationContext buildServiceProviderAuthenticationSteps()
				throws AuthenticationException {

			return serviceProvider(this.clientId).build();
		}

	}

	public void setUser(User user) {

		this.user = user;
	}
	public User getUser() {

		return this.user;
	}

	public int getNextAuthenticationStepId() {
		return nextAuthenticationStepId;
	}

	public void setNextAuthenticationStepId(int nextAuthenticationStepId) {
		this.nextAuthenticationStepId = nextAuthenticationStepId;
	}

	public void setMultiAttributeLoginClaim(String multiAttributeLoginClaimURI) {

		this.multiAttributeLoginClaimURI = multiAttributeLoginClaimURI;
	}
	public String getMultiAttributeLoginClaim() {

		return this.multiAttributeLoginClaimURI;
	}
	public void setAuthenticationSteps(LinkedHashMap<Integer, List<String>> authenticationSteps) {

		this.authenticationSteps = authenticationSteps;
	}

}
