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

package org.wso2.carbon.identity.oauth2.grant.rest.core.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.ConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationServerException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.internal.AuthenticationServiceDataHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * This Util class is used to define the reusable methods in core component.
 */
public class RestAuthUtil {
    private static final Log LOG = LogFactory.getLog(RestAuthUtil.class);
    private LinkedHashMap<Integer , IdentityProvider[]> federatedAuthenticators;

    /**
     * This method returns the SHA-256 hash of a given string.
     *
     * @param text plain text.
     * @return SHA-256 hash value of the given plain text.
     */
    public static String getHash(String text) {
        return DigestUtils.sha256Hex(text);
    }

    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Flow Id hash: %s.", RestAuthUtil.getHash(uuid)));
        }
        return uuid;
    }

    /**
     * Read configurations and populate ConfigDTO object.
     *
     * @throws AuthenticationException Throws upon an issue on while reading configs.
     */
    public static void readConfigurations() throws AuthenticationException {

        InputStream inputStream = null;
        Properties properties;
        String configFilePath = Constants.CONFIG_FILE_PATH + Constants.CONFIG_FILE_NAME;

        try {
            properties = new Properties();

            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format(" %s file loaded from %s.", Constants.CONFIG_FILE_NAME,
                            Constants.CONFIG_FILE_PATH + Constants.CONFIG_FILE_NAME));
                }
                inputStream = new FileInputStream(configFile);
                properties.load(inputStream);
            } else {
                LOG.info(String.format(" %s file has NOT been loaded from %s.", Constants.CONFIG_FILE_NAME,
                        Constants.CONFIG_FILE_PATH + Constants.CONFIG_FILE_NAME));
            }
        } catch (FileNotFoundException e) {
            LOG.error("Failed to load service configurations.", e);
            throw handleServerException(
                    Constants.ErrorMessage.SERVER_CONFIG_FILE_NOT_FOUND_ERROR, configFilePath);
        } catch (IOException e) {
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_CONFIG_LOADING_IO_ERROR, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_CONFIG_FILE_CLOSURE_IO_ERROR,
                            Constants.CONFIG_FILE_NAME, e);
                }
            }
        }
        sanitizeAndPopulateConfigs(properties);
    }

    private static void sanitizeAndPopulateConfigs(Properties properties) {

        ConfigsDTO configs = AuthenticationServiceDataHolder.getConfigs();

        String isEnabledValue = StringUtils.trim(properties.getProperty(Constants.REST_AUTH_ENABLED));
        boolean isEnabled = StringUtils.isNotEmpty(isEnabledValue) ?
                Boolean.parseBoolean(isEnabledValue) : Constants.DEFAULT_REST_AUTH_ENABLED;
        configs.setEnabled(isEnabled);

        String showFailureReasonValue = StringUtils.trim(properties.getProperty(Constants.AUTH_SHOW_FAILURE_REASON));
        boolean showFailureReason = StringUtils.isNotEmpty(showFailureReasonValue) ?
                Boolean.parseBoolean(showFailureReasonValue) : Constants.DEFAULT_AUTH_SHOW_FAILURE_REASON;
        configs.setShowFailureReason(showFailureReason);

        String otpValidityPeriodValue =
                StringUtils.trim(properties.getProperty(Constants.FLOW_ID_VALIDITY_PERIOD));
        int otpValidityPeriod = StringUtils.isNumeric(otpValidityPeriodValue) ?
                Integer.parseInt(otpValidityPeriodValue) * 1000 : Constants.DEFAULT_FLOW_ID_VALIDITY_PERIOD;
        configs.setFlowIdValidityPeriod(otpValidityPeriod);

        String timestampSkewValue = StringUtils.trim(properties.getProperty(Constants.FLOW_ID_TIMESTAMP_SKEW));
        int timestampSkew = StringUtils.isNumeric(timestampSkewValue) ?
                Integer.parseInt(timestampSkewValue) * 1000 : Constants.DEFAULT_FLOW_ID_TIMESTAMP_SKEW;
        configs.setTimestampSkew(timestampSkew);

    }

    public static AuthenticationClientException handleClientException(Constants.ErrorMessage error) {

        String description = error.getDescription();
        return new AuthenticationClientException(error.getCode(), error.getMessage(), description);
    }

    public static AuthenticationClientException handleClientException(Constants.ErrorMessage error, String data) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new AuthenticationClientException(error.getCode(), error.getMessage(), description);
    }

    public static AuthenticationClientException handleClientException(String rawError, String data) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(rawError.split(";")[2], data);
        } else {
            description = rawError.split(";")[2];
        }
        return new AuthenticationClientException
                (rawError.split(";")[0], rawError.split(";")[1], description);
    }

    public static AuthenticationClientException handleClientException(Constants.ErrorMessage error, String data,
            Throwable e) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new AuthenticationClientException(error.getCode(), error.getMessage(), description, e);
    }

    public static AuthenticationServerException handleServerException(Constants.ErrorMessage error, String data,
            Throwable e) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new AuthenticationServerException(error.getCode(), error.getMessage(), description, e);
    }

    public static AuthenticationServerException handleServerException(Constants.ErrorMessage error, String data) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new AuthenticationServerException(error.getCode(), error.getMessage(), description);
    }

    public static AuthenticationServerException handleServerException(Constants.ErrorMessage error, Throwable e) {

        String description = error.getDescription();
        return new AuthenticationServerException(error.getCode(), error.getMessage(), description, e);
    }

    public static String getTenantDomain() {
        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
    }

    private static boolean isValidClientId(ServiceProvider serviceProvider) {

        return serviceProvider.getApplicationResourceId() != null;
    }

    /**
     * This method retrieves Service Provider using App Id.
     *
     * @param serviceProviderAppId 		AppId of the Service Provider.
     * @return service provider.
     * @throws AuthenticationException if any server or client error occurred.
     */
    public static ServiceProvider getServiceProviderByAppId (Integer serviceProviderAppId)
            throws AuthenticationException {

        ServiceProvider serviceProvider;
        try {
            serviceProvider = ApplicationManagementServiceImpl.getInstance().getServiceProvider(serviceProviderAppId);
        } catch (IdentityApplicationManagementException e) {
            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_SP_ERROR,
                    String.format("Error while retrieving service provider for the App ID : %s.",
                            serviceProviderAppId), e);
        }

        if (isValidClientId(serviceProvider)) {
            return serviceProvider;
        } else {
            throw RestAuthUtil.handleClientException(Constants.ErrorMessage.SERVER_INVALID_APP_ID,
                    String.valueOf(serviceProviderAppId));
        }

    }

    /**
     * This method retrieves Service Provider using clientId.
     *
     * @param clientId 		clientId of the Service Provider.
     * @return service provider.
     * @throws AuthenticationException if any server or client error occurred.
     */
    public static ServiceProvider getServiceProviderByClientId(String clientId) throws AuthenticationException {

        ServiceProvider serviceProvider;

        try {
            serviceProvider = ApplicationManagementServiceImpl.getInstance().getServiceProviderByClientId(clientId,
                    Constants.CLIENT_TYPE, getTenantDomain());
        } catch (IdentityApplicationManagementException e) {

            throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_RETRIEVING_SP_ERROR ,
                    String.format("Error while retrieving service provider for the clientId : %s.", clientId), e);
        }

        if (isValidClientId(serviceProvider)) {
            return serviceProvider;
        } else {
            throw RestAuthUtil.handleClientException(
                    Constants.ErrorMessage.CLIENT_INVALID_CLIENT_ID, clientId);
        }

    }


    public static LinkedHashMap<Integer, List<String>> getAuthStepsForSP(ServiceProvider serviceProvider) {

        AuthenticationStep[] authSteps = null;
        LinkedHashMap<Integer, List<String>> authenticationSteps = new LinkedHashMap<>();
        LinkedHashMap<Integer , IdentityProvider[]> federatedAuthenticators = new LinkedHashMap<>();

        if (org.apache.commons.lang.StringUtils.isNotBlank(serviceProvider.getApplicationResourceId())) {
            authSteps = serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps();
        }

        if (ArrayUtils.isNotEmpty(authSteps)) {
            int stepCount = 1;
            for (AuthenticationStep step : authSteps) {
                List<String> authenticators = new ArrayList<>();
                LocalAuthenticatorConfig[] localAuthenticatorConfigs = step.getLocalAuthenticatorConfigs();
                IdentityProvider[] fedIDPs = step.getFederatedIdentityProviders();

                if (ArrayUtils.isNotEmpty(localAuthenticatorConfigs)) {
                    for (LocalAuthenticatorConfig lclAuthenticator: localAuthenticatorConfigs) {
                        String lclAuthenticatorName = lclAuthenticator.getName();
                        authenticators.add(lclAuthenticatorName);
                    }
                }
                if (ArrayUtils.isNotEmpty(fedIDPs)) {
                     federatedAuthenticators.put(stepCount, fedIDPs);
                    for (IdentityProvider fedIdp: fedIDPs) {
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


    public static LinkedHashMap<Integer, IdentityProvider[]> getFederatedIdentityProviders(int serviceProviderAppId)
            throws AuthenticationException {

        ServiceProvider serviceProvider = getServiceProviderByAppId(serviceProviderAppId);
        AuthenticationStep[] authSteps = null;
        LinkedHashMap<Integer , IdentityProvider[]> federatedAuthenticators = new LinkedHashMap<>();

        if (org.apache.commons.lang.StringUtils.isNotBlank(serviceProvider.getApplicationResourceId())) {
            authSteps = serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps();
        }

        if (ArrayUtils.isNotEmpty(authSteps)) {
            int stepCount = 1;
            for (AuthenticationStep step : authSteps) {
                IdentityProvider[] fedIDPs = step.getFederatedIdentityProviders();

                if (ArrayUtils.isNotEmpty(fedIDPs)) {
                    federatedAuthenticators.put(stepCount, fedIDPs);
                }
                stepCount++;
            }
        }
        return federatedAuthenticators;

    }
}
