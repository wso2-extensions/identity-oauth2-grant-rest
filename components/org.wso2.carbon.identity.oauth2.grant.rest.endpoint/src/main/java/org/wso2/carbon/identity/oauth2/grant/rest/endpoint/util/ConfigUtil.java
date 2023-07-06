package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.dto.RestAuthEndpointErrorDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class ontains the configurations from proprties file.
 */
public class ConfigUtil {

    private static final Log LOG = LogFactory.getLog(ConfigUtil.class);

    public Properties readConfigurations()  {

        InputStream inputStream = null;
        Properties properties = null;
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
        } catch (IOException e) {
            LOG.error("Error while loading teh config file");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error("Error while reading teh config file");
                }
            }
        }
        return properties;
    }

    public RestAuthEndpointErrorDTO sanitizeAndPopulateConfigs(Properties properties) {

        RestAuthEndpointErrorDTO errorDTO = new RestAuthEndpointErrorDTO();

        String clientMandatoryParamsEmpty = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_MANDATORY_PARAMS_EMPTY));
        errorDTO.setClientMandatoryParamsEmpty(clientMandatoryParamsEmpty);

        String clientInvalidAuthenticator = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INVALID_AUTHENTICATOR));
        errorDTO.setClientInvalidAuthenticator(clientInvalidAuthenticator);

        String clientInvalidClientId = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INVALID_CLIENT_ID));
        errorDTO.setClientInvalidClientId(clientInvalidClientId);

        String clientIncorrectUserCredentials = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INCORRECT_USER_CREDENTIALS));
        errorDTO.setClientIncorrectUserCredentials(clientIncorrectUserCredentials);

        String clientInactiveFlowId = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INACTIVE_FLOW_ID));
        errorDTO.setClientInactiveFlowId(clientInactiveFlowId);

        String clientInvalidFlowId = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INVALID_FLOW_ID));
        errorDTO.setClientInvalidFlowId(clientInvalidFlowId);

        String clientAuthStepOutOfBound = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_AUTH_STEP_OUT_OF_BOUND));
        errorDTO.setClientAuthStepOutOfBound(clientAuthStepOutOfBound);

        String clientUnSupportedAuthenticator = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_UNSUPPORTED_AUTHENTICATOR));
        errorDTO.setClientUnSupportedAuthenticator(clientUnSupportedAuthenticator);

        String clientExpiredFlowId = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_EXPIRED_FLOW_ID));
        errorDTO.setClientExpiredFlowId(clientExpiredFlowId);

        String clientLockedUserAccount = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_LOCKED_USER_ACCOUNT));
        errorDTO.setClientLockedUserAccount(clientLockedUserAccount);

        String clientDisabledUserAccount = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_DISABLED_USER_ACCOUNT));
        errorDTO.setClientDisabledUserAccount(clientDisabledUserAccount);

        String clientFlowIdMismatch = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_FLOW_ID_MISMATCH));
        errorDTO.setClientFlowIdMismatch(clientFlowIdMismatch);

        String clientInvalidUserCredentials = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_INVALID_USER_CREDENTIALS));
        errorDTO.setClientInvalidUserCredentials(clientInvalidUserCredentials);

        String clientCrossTenantAccessRestriction = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_CROSS_TENANT_ACCESS_RESTRICTION));
        errorDTO.setClientCrossTenantAccessRestriction(clientCrossTenantAccessRestriction);

        String clientUsernameResolveFailed = StringUtils.trim(properties.getProperty
                (Constants.CLIENT_USERNAME_RESOLVE_FAIL));
        errorDTO.setClientUsernameResolveFailed(clientUsernameResolveFailed);

        return errorDTO;
    }
}
