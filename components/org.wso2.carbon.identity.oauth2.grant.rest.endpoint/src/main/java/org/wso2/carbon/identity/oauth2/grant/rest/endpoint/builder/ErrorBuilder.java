package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.builder;

import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.dto.RestAuthEndpointErrorDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ConfigUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ErrorUtil;
import java.util.Properties;

/**
 * This class is used to map built in errors with customize errors mentioned in property file.
 */
public class ErrorBuilder {

    public static ErrorUtil buildError(String errorCode) {

        ConfigUtil configUtil = new ConfigUtil();
        Properties properties = configUtil.readConfigurations();
        RestAuthEndpointErrorDTO configsDTO = configUtil.sanitizeAndPopulateConfigs(properties);
        ErrorUtil errorUtil = new ErrorUtil();

        switch (errorCode) {
            case "E-60001" :
                errorUtil.setErrorCode(configsDTO.getClientMandatoryParamsEmpty().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientMandatoryParamsEmpty().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientMandatoryParamsEmpty().split(";")[2]);
                break;
            case "E-60002" :
                errorUtil.setErrorCode(configsDTO.getClientInvalidAuthenticator().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientInvalidAuthenticator().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientInvalidAuthenticator().split(";")[2]);
                break;
            case "E-60003" :
                errorUtil.setErrorCode(configsDTO.getClientInvalidClientId().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientInvalidClientId().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientInvalidClientId().split(";")[2]);
                break;
            case "E-60004" :
                errorUtil.setErrorCode(configsDTO.getClientIncorrectUserCredentials().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientIncorrectUserCredentials().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientIncorrectUserCredentials().split(";")[2]);
                break;
            case "E-60005" :
                errorUtil.setErrorCode(configsDTO.getClientInactiveFlowId().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientInactiveFlowId().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientInactiveFlowId().split(";")[2]);
                break;
            case "E-60006" :
                errorUtil.setErrorCode(configsDTO.getClientInvalidFlowId().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientInvalidFlowId().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientInvalidFlowId().split(";")[2]);
                break;
            case "E-60007" :
                errorUtil.setErrorCode(configsDTO.getClientAuthStepOutOfBound().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientAuthStepOutOfBound().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientAuthStepOutOfBound().split(";")[2]);
                break;
            case "E-60008" :
                errorUtil.setErrorCode(configsDTO.getClientUnSupportedAuthenticator().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientUnSupportedAuthenticator().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientUnSupportedAuthenticator().split(";")[2]);
                break;
            case "E-60009" :
                errorUtil.setErrorCode(configsDTO.getClientExpiredFlowId().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientExpiredFlowId().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientExpiredFlowId().split(";")[2]);
                break;
            case "E-60010" :
                errorUtil.setErrorCode(configsDTO.getClientLockedUserAccount().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientLockedUserAccount().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientLockedUserAccount().split(";")[2]);
                break;
            case "E-60011" :
                errorUtil.setErrorCode(configsDTO.getClientDisabledUserAccount().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientDisabledUserAccount().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientDisabledUserAccount().split(";")[2]);
                break;
            case "E-60012" :
                errorUtil.setErrorCode(configsDTO.getClientFlowIdMismatch().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientFlowIdMismatch().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientFlowIdMismatch().split(";")[2]);
                break;
            case "E-60013" :
                errorUtil.setErrorCode(configsDTO.getClientInvalidUserCredentials().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientInvalidUserCredentials().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientInvalidUserCredentials().split(";")[2]);
                break;
            case "E-60014" :
                errorUtil.setErrorCode(configsDTO.getClientCrossTenantAccessRestriction().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientCrossTenantAccessRestriction().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientCrossTenantAccessRestriction().split(";")[2]);
                break;
            case "E-60015" :
                errorUtil.setErrorCode(configsDTO.getClientUsernameResolveFailed().split(";")[0]);
                errorUtil.setErrorMessage(configsDTO.getClientUsernameResolveFailed().split(";")[1]);
                errorUtil.setErrorDescription(configsDTO.getClientUsernameResolveFailed().split(";")[2]);
        }

        return errorUtil;
    }
}
