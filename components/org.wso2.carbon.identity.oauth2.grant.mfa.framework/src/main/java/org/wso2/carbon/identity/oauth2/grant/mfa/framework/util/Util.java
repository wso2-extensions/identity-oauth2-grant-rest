package org.wso2.carbon.identity.oauth2.grant.mfa.framework.util;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.ConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthServerException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.UUID;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal.MFAAuthServiceDataHolder;

public class Util {
    private static final Log log = LogFactory.getLog(Util.class);

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
        if (log.isDebugEnabled()) {
            log.debug(String.format("MFA Token hash: %s.", Util.getHash(uuid)));
        }
        return uuid;
    }

    /**
     * Read configurations and populate ConfigDTO object.
     *
     * @throws MFAAuthException Throws upon an issue on while reading configs.
     */
    public static void readConfigurations() throws MFAAuthException {

        InputStream inputStream = null;
        Properties properties;
        String configFilePath = Constants.MFA_CONFIG_PATH + Constants.CONFIG_FILE_NAME;

        try {
            properties = new Properties();

            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format(" %s file loaded from %s.", Constants.CONFIG_FILE_NAME,
                            Constants.MFA_CONFIG_PATH + Constants.CONFIG_FILE_NAME));
                }
                inputStream = new FileInputStream(configFile);
                properties.load(inputStream);
            } else {
                log.error(String.format(" %s file has NOT been loaded from %s.", Constants.CONFIG_FILE_NAME,
                        Constants.MFA_CONFIG_PATH + Constants.CONFIG_FILE_NAME));
            }
        } catch (FileNotFoundException e) {
            log.error("Failed to load service configurations.", e);
            throw handleServerException(
                    Constants.ErrorMessage.SERVER_CONFIG_FILE_NOT_FOUND_ERROR, configFilePath);
        } catch (IOException e) {
            throw Util.handleServerException(Constants.ErrorMessage.SERVER_CONFIG_LOADING_IO_ERROR, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw Util.handleServerException(Constants.ErrorMessage.SERVER_CONFIG_FILE_CLOSURE_IO_ERROR,
                            Constants.CONFIG_FILE_NAME, e);
                }
            }
        }
        sanitizeAndPopulateConfigs(properties);
    }

    private static void sanitizeAndPopulateConfigs(Properties properties) {

        ConfigsDTO configs = MFAAuthServiceDataHolder.getConfigs();

        boolean isEnabled = Boolean.parseBoolean(StringUtils.trim(
                properties.getProperty(Constants.MFA_AUTH_ENABLED)));
        configs.setEnabled(isEnabled);

        boolean showFailureReason = Boolean.parseBoolean(StringUtils.trim(
                properties.getProperty(Constants.MFA_AUTH_SHOW_FAILURE_REASON)));
        configs.setShowFailureReason(showFailureReason);

        String otpValidityPeriodValue =
                StringUtils.trim(properties.getProperty(Constants.MFA_TOKEN_VALIDITY_PERIOD));
        int otpValidityPeriod = StringUtils.isNumeric(otpValidityPeriodValue) ?
                Integer.parseInt(otpValidityPeriodValue) * 1000 : Constants.DEFAULT_MFA_TOKEN_VALIDITY_PERIOD;
        configs.setMfaTokenValidityPeriod(otpValidityPeriod);

        String timestampSkewValue = StringUtils.trim(properties.getProperty(Constants.MFA_TOKEN_TIMESTAMP_SKEW));
        int timestampSkew = StringUtils.isNumeric(timestampSkewValue) ?
                Integer.parseInt(timestampSkewValue) * 1000 : Constants.DEFAULT_MFA_TOKEN_TIMESTAMP_SKEW;
        configs.setTimestampSkew(timestampSkew);

        if (isLocalCustomAuthenticatorAvailable(properties)) {
            String customLocalAuthenticatorName = StringUtils.trim(properties.getProperty
                    (Constants.MFA_CUSTOM_LOCAL_AUTHENTICATOR));
            configs.setCustomLocalAuthenticatorName(customLocalAuthenticatorName);
        }

    }

    public static boolean isLocalCustomAuthenticatorAvailable(Properties properties){
        return StringUtils.isNotBlank(properties.getProperty(Constants.MFA_CUSTOM_LOCAL_AUTHENTICATOR));
    }

    public static MFAAuthClientException handleClientException(Constants.ErrorMessage error) {

        String description = error.getDescription();

        return new MFAAuthClientException(error.getCode(), error.getMessage(), description);
    }

    public static MFAAuthClientException handleClientException(Constants.ErrorMessage error, String data) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new MFAAuthClientException(error.getCode(), error.getMessage(), description);
    }

    public static MFAAuthClientException handleClientException(Constants.ErrorMessage error, String data,
                                                                    Throwable e) {
        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new MFAAuthClientException(error.getCode(), error.getMessage(), description, e);
    }

    public static MFAAuthServerException handleServerException(Constants.ErrorMessage error, String data,
                                                                    Throwable e) {
        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new MFAAuthServerException(error.getCode(), error.getMessage(), description, e);
    }

    public static MFAAuthServerException handleServerException(Constants.ErrorMessage error, String data) {

        String description;
        if (StringUtils.isNotBlank(data)) {
            description = String.format(error.getDescription(), data);
        } else {
            description = error.getDescription();
        }
        return new MFAAuthServerException(error.getCode(), error.getMessage(), description);
    }

    public static MFAAuthServerException handleServerException(Constants.ErrorMessage error, Throwable e) {
        String description = error.getDescription();
        return new MFAAuthServerException(error.getCode(), error.getMessage(), description, e);
    }
}
