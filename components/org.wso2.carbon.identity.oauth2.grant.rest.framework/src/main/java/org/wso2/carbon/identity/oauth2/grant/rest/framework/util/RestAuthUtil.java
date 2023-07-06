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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.ConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationServerException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.internal.AuthenticationServiceDataHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * This Util class is used to define the reusable methods in framework component.
 */
public class RestAuthUtil {
    private static final Log LOG = LogFactory.getLog(RestAuthUtil.class);

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
                LOG.error(String.format(" %s file has NOT been loaded from %s.", Constants.CONFIG_FILE_NAME,
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

        boolean isEnabled = Boolean.parseBoolean(StringUtils.trim(
                properties.getProperty(Constants.REST_AUTH_ENABLED)));
        configs.setEnabled(isEnabled);

        boolean showFailureReason = Boolean.parseBoolean(StringUtils.trim(
                properties.getProperty(Constants.AUTH_SHOW_FAILURE_REASON)));
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
}
