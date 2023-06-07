package org.wso2.carbon.identity.oauth2.grant.mfa.framework.context;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.*;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementServiceImpl;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal.MFAAuthServiceDataHolder;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AuthenticationContext {

	//Auth Flow related variables
	private String authenticator;
	private boolean isAuthFlowCompleted;

	//User related variables
	private String username;
	private String userId;
	private int userTenantId;
	private LinkedHashMap<Integer, String> authenticatedSteps;

	//Service Provider related variables
	private ServiceProvider serviceProvider;
	private LinkedHashMap<Integer, List<String>> authenticationSteps;
	private String clientId;
	private int spTenantId;

	//MFA Token related variables
	private int mfaTokenValidityPeriod;
	private String mfaToken;
	private String mfaTokenId;
	private String mfaTokenState;
	private String newMfaTokenId;
	private String newMfaToken;
	private User user;

	//OTP related variables
	private String otp;
	private boolean isValidOtp;

	private AuthenticationContext(Builder builder) {
		this.authenticator = builder.authenticator;
		this.isAuthFlowCompleted = builder.isAuthFlowCompleted;
		this.username = builder.username;
		this.userId = builder.userId;
		this.userTenantId = builder.userTenantId;
		this.authenticatedSteps = builder.authenticatedSteps;
		this.serviceProvider = builder.serviceProvider;
		this.authenticationSteps = builder.authenticationSteps;
		this.clientId = builder.clientId;
		this.spTenantId = builder.spTenantId;
		this.mfaTokenValidityPeriod = builder.mfaTokenValidityPeriod;
		this.mfaToken = builder.mfaToken;
		this.mfaTokenId = builder.mfaTokenId;
		this.mfaTokenState = builder.mfaTokenState;
		this.newMfaTokenId = builder.newMfaTokenId;
		this.newMfaToken = builder.newMfaToken;
		this.otp = builder.otp;
		this.isValidOtp = builder.isValidOtp;
	}

	//Authentication Context getters
	public String getAuthenticator() {
		return authenticator;
	}

	public boolean isAuthFlowCompleted() {
		return isAuthFlowCompleted;
	}

	public String getUsername() {
		return username;
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

	public int getMfaTokenValidityPeriod() {
		return mfaTokenValidityPeriod;
	}

	public String getMfaToken() {
		return mfaToken;
	}

	public String getMfaTokenId() {
		return mfaTokenId;
	}

	public String getMfaTokenState() {
		return mfaTokenState;
	}

	public String getNewMfaTokenId() {
		return newMfaTokenId;
	}

	public String getNewMfaToken() {
		return newMfaToken;
	}

	public String getOtp() {
		return otp;
	}

	public boolean isValidOtp() {
		return isValidOtp;
	}

	//Authentication Context setters
	public AuthenticationContext setAuthFlowCompleted(boolean isAuthFlowCompleted) {
		this.isAuthFlowCompleted = isAuthFlowCompleted;
		return this;
	}

	public AuthenticationContext setUserId (String userId) {
		this.userId = userId;
		return this;
	}

	public AuthenticationContext setUserTenantId (int userTenantId) {
		this.userTenantId= userTenantId;
		return this;
	}

	public AuthenticationContext setAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {
		this.authenticatedSteps = authenticatedSteps;
		return this;
	}

	public AuthenticationContext setServiceProvider (int serviceProviderAppId) throws MFAAuthException {
		this.serviceProvider = getServiceProviderByAppId(serviceProviderAppId);
		setAuthenticationSteps(serviceProvider);
		return this;
	}

	public AuthenticationContext setAuthenticationSteps (ServiceProvider serviceProvider) {
		this.authenticationSteps = getAuthStepsforSP(serviceProvider);
		return this;
	}

	public AuthenticationContext setSpTenantId (int spTenantId) {
		this.spTenantId= spTenantId;
		return this;
	}

	public AuthenticationContext setMfaTokenId(String mfaTokenId) {
		this.mfaTokenId = mfaTokenId;
		return this;
	}

	public AuthenticationContext setNewMfaToken(String newMfaToken) {
		this.newMfaToken = newMfaToken;
		return this;
	}

	public AuthenticationContext setOtp(String otp) {
		this.otp = otp;
		return this;
	}

	public AuthenticationContext setValidOtp (boolean isValidOtp) {
		this.isValidOtp = isValidOtp;
		return this;
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
		private int mfaTokenValidityPeriod;
		private String mfaToken;
		private String mfaTokenId;
		private String mfaTokenState;
		private String newMfaTokenId;
		private String newMfaToken;
		private String otp;
		private boolean isValidOtp;

		public Builder (HashMap<String, String> params) throws MFAAuthException {
			for (HashMap.Entry<String, String> entry : params.entrySet()) {
				if (entry.getValue() == null) {
					String missingParam = entry.getKey();
					throw Util.handleClientException(
							Constants.ErrorMessage.CLIENT_MANDATORY_VALIDATION_PARAMETERS_EMPTY, missingParam);
				}
				try {
					Method method = this.getClass().getDeclaredMethod(entry.getKey(),String.class);
					method.invoke(this, entry.getValue());
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					throw Util.handleServerException(Constants.ErrorMessage.SERVER_REQUEST_PARAM_READING_ERROR,
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

		public Builder serviceProvider (String clientId) throws MFAAuthException {
			if (clientId != null) {
				this.serviceProvider = getServiceProviderByClientId(clientId);
				authenticationSteps(serviceProvider);
			}
			return this;
		}

		public Builder authenticationSteps (ServiceProvider serviceProvider) {
			this.authenticationSteps = getAuthStepsforSP(serviceProvider);
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

		public Builder mfaTokenValidityPeriod(int mfaTokenValidityPeriod) {
			this.mfaTokenValidityPeriod = mfaTokenValidityPeriod;
			return this;
		}

		public Builder mfaToken(String mfaToken) {
			this.mfaToken = mfaToken;
			return this;
		}

		public Builder mfaTokenId(String mfaTokenId) {
			this.mfaTokenId = mfaTokenId;
			return this;
		}

		public Builder mfaTokenState(String mfaTokenState) {
			this.mfaTokenState = mfaTokenState;
			return this;
		}

		public Builder newMfaToken(String newMfaToken) {
			this.newMfaToken = newMfaToken;
			return this;
		}

		public Builder newMfaTokenId(String newMfaTokenId) {
			this.newMfaTokenId = newMfaTokenId;
			return this;
		}

		public Builder otp (String otp) {
			this.otp = otp;
			return this;
		}

		public Builder setValidOtp (boolean isValidOtp) {
			this.isValidOtp = isValidOtp;
			return this;
		}

		private AuthenticationContext build() {

			return new AuthenticationContext(this);

		}

		public AuthenticationContext buildForBasicAuth() throws MFAAuthException {

			return newMfaToken(Util.generateUUID())
					.newMfaTokenId(Util.generateUUID())
					.serviceProvider(this.clientId)
					.mfaTokenState(Constants.MFA_TOKEN_STATE_ACTIVE)
					.mfaTokenValidityPeriod(MFAAuthServiceDataHolder.getConfigs().getMfaTokenValidityPeriod())
					.spTenantId(IdentityTenantUtil.getTenantId(getTenantDomain()))
					.build();
		}

		public AuthenticationContext buildForMFAInitialize() {
			return newMfaTokenId(Util.generateUUID())
					.build();
		}

		public AuthenticationContext buildForMFAValidate() {
			return newMfaTokenId(Util.generateUUID())
					.newMfaToken(Util.generateUUID())
					.build();
		}

	}

	/**
	 * This method retrieves Service Provider using Client Id.
	 *
	 * @param clientId 		clientId of the Service Provider.
	 * @return service provider.
	 * @throws MFAAuthException if any server or client error occurred.
	 */
	private static ServiceProvider getServiceProviderByClientId(String clientId) throws MFAAuthException {

		ServiceProvider serviceProvider;

		try {
			serviceProvider =
					ApplicationManagementServiceImpl.getInstance().getServiceProviderByClientId(clientId,
							Constants.CLIENT_TYPE, getTenantDomain());
		} catch (IdentityApplicationManagementException e) {
			throw Util.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_SP_ERROR,
					String.format("Error while retrieving service provider for the clientId : %s.", clientId), e);
		}

		if (isValidClientId(serviceProvider)) {
			return serviceProvider;
		} else {
			throw Util.handleClientException(
					Constants.ErrorMessage.CLIENT_INVALID_CLIENT_ID, clientId);
		}

	}

	/**
	 * This method retrieves Service Provider using App Id.
	 *
	 * @param serviceProviderAppId 		App Id of the Service Provider.
	 * @return service provider.
	 * @throws MFAAuthException if any server or client error occurred.
	 */
	public static ServiceProvider getServiceProviderByAppId(Integer serviceProviderAppId) throws MFAAuthException {

		ServiceProvider serviceProvider;

		try {

			serviceProvider =
					ApplicationManagementServiceImpl.getInstance().getServiceProvider(serviceProviderAppId);

		} catch (IdentityApplicationManagementException e) {
			throw Util.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_SP_ERROR,
					String.format("Error while retrieving service provider for the App ID : %s.",
							serviceProviderAppId), e);
		}

		if (isValidClientId(serviceProvider)) {
			return serviceProvider;
		} else {
			throw Util.handleClientException(
					Constants.ErrorMessage.SERVER_INVALID_APP_ID, String.valueOf(serviceProviderAppId));
		}

	}

	/**
	 * This method validates the client id.
	 *
	 * @param serviceProvider 		Service Provider.
	 * @return whether the client id exists or not.
	 */
	private static boolean isValidClientId(ServiceProvider serviceProvider) {

		return serviceProvider.getApplicationResourceId() != null;
	}

	/**
	 * This method returns the Tenant Domain.
	 *
	 * @return Tenant Domain.
	 */
	private static String getTenantDomain() {

		return PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
	}

	/**
	 * This method retrieves Authentication Steps defined for the Service Provider.
	 *
	 * @param serviceProvider Service Provider.
	 * @return Authentication Steps.
	 */
	public static LinkedHashMap<Integer, List<String>> getAuthStepsforSP(ServiceProvider serviceProvider) {

		AuthenticationStep[] authSteps = null;
		LinkedHashMap<Integer, List<String>> authenticationSteps = new LinkedHashMap<>();

		if (serviceProvider.getApplicationResourceId() != null) {
			authSteps = serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps();
		}

		if (authSteps != null && authSteps.length > 0) {
			int stepCount = 1;
			for (AuthenticationStep step : authSteps) {
				List<String> authenticators = new ArrayList<>();
				LocalAuthenticatorConfig[] lclAuthenticators = step.getLocalAuthenticatorConfigs();
				IdentityProvider[] fedIdps = step.getFederatedIdentityProviders();
				if (lclAuthenticators != null && lclAuthenticators.length > 0) {
					for (LocalAuthenticatorConfig lclAuthenticator: lclAuthenticators) {
						String lclAuthenticatorName = lclAuthenticator.getName();
						authenticators.add(lclAuthenticatorName);
					}
				}
				if (fedIdps != null && fedIdps.length > 0) {
					for (IdentityProvider fedIdp: fedIdps) {
						String fedIdpName = fedIdp.getIdentityProviderName();
						authenticators.add(fedIdpName);
					}
				}
				authenticationSteps.put(stepCount, authenticators);
				stepCount++;
			}
		}
		return authenticationSteps;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return this.user;
	}
}
