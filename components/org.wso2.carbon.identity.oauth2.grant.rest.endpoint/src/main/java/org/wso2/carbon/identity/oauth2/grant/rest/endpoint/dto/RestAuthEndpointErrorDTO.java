package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.dto;

/**
 * This DTO class contains a model of Errors.
 */
public class RestAuthEndpointErrorDTO {

    private String clientMandatoryParamsEmpty;
    private String clientInvalidAuthenticator;
    private String clientInvalidClientId;
    private String clientIncorrectUserCredentials;
    private String clientInactiveFlowId;
    private String clientInvalidFlowId;
    private String clientAuthStepOutOfBound;
    private String clientUnSupportedAuthenticator;
    private String clientExpiredFlowId;
    private String clientLockedUserAccount;
    private String clientDisabledUserAccount;
    private String clientFlowIdMismatch;
    private String clientInvalidUserCredentials;
    private String clientCrossTenantAccessRestriction;
    private String clientUsernameResolveFailed;

    public String getClientMandatoryParamsEmpty() {

        return clientMandatoryParamsEmpty;
    }

    public void setClientMandatoryParamsEmpty(String clientMandatoryParamsEmpty) {

        this.clientMandatoryParamsEmpty = clientMandatoryParamsEmpty;
    }

    public String getClientInvalidAuthenticator() {

        return clientInvalidAuthenticator;
    }

    public void setClientInvalidAuthenticator(String clientInvalidAuthenticator) {

        this.clientInvalidAuthenticator = clientInvalidAuthenticator;
    }

    public String getClientInvalidClientId() {

        return clientInvalidClientId;
    }

    public void setClientInvalidClientId(String clientInvalidClientId) {

        this.clientInvalidClientId = clientInvalidClientId;
    }

    public String getClientIncorrectUserCredentials() {

        return clientIncorrectUserCredentials;
    }

    public void setClientIncorrectUserCredentials(String clientIncorrectUserCredentials) {

        this.clientIncorrectUserCredentials = clientIncorrectUserCredentials;
    }

    public String getClientInactiveFlowId() {

        return clientInactiveFlowId;
    }

    public void setClientInactiveFlowId(String clientInactiveFlowId) {

        this.clientInactiveFlowId = clientInactiveFlowId;
    }

    public String getClientInvalidFlowId() {

        return clientInvalidFlowId;
    }

    public void setClientInvalidFlowId(String clientInvalidFlowId) {

        this.clientInvalidFlowId = clientInvalidFlowId;
    }

    public String getClientAuthStepOutOfBound() {

        return clientAuthStepOutOfBound;
    }

    public void setClientAuthStepOutOfBound(String clientAuthStepOutOfBound) {

        this.clientAuthStepOutOfBound = clientAuthStepOutOfBound;
    }

    public String getClientUnSupportedAuthenticator() {

        return clientUnSupportedAuthenticator;
    }

    public void setClientUnSupportedAuthenticator(String clientUnSupportedAuthenticator) {

        this.clientUnSupportedAuthenticator = clientUnSupportedAuthenticator;
    }

    public String getClientExpiredFlowId() {

        return clientExpiredFlowId;
    }

    public void setClientExpiredFlowId(String clientExpiredFlowId) {

        this.clientExpiredFlowId = clientExpiredFlowId;
    }

    public String getClientLockedUserAccount() {

        return clientLockedUserAccount;
    }

    public void setClientLockedUserAccount(String clientLockedUserAccount) {

        this.clientLockedUserAccount = clientLockedUserAccount;
    }

    public String getClientDisabledUserAccount() {

        return clientDisabledUserAccount;
    }

    public void setClientDisabledUserAccount(String clientDisabledUserAccount) {

        this.clientDisabledUserAccount = clientDisabledUserAccount;
    }

    public String getClientFlowIdMismatch() {

        return clientFlowIdMismatch;
    }

    public void setClientFlowIdMismatch(String clientFlowIdMismatch) {

        this.clientFlowIdMismatch = clientFlowIdMismatch;
    }

    public String getClientInvalidUserCredentials() {

        return clientInvalidUserCredentials;
    }

    public void setClientInvalidUserCredentials(String clientInvalidUserCredentials) {

        this.clientInvalidUserCredentials = clientInvalidUserCredentials;
    }

    public String getClientCrossTenantAccessRestriction() {

        return clientCrossTenantAccessRestriction;
    }

    public void setClientCrossTenantAccessRestriction(String clientCrossTenantAccessRestriction) {

        this.clientCrossTenantAccessRestriction = clientCrossTenantAccessRestriction;
    }

    public String getClientUsernameResolveFailed() {

        return clientUsernameResolveFailed;
    }

    public void setClientUsernameResolveFailed(String clientUsernameResolveFailed) {

        this.clientUsernameResolveFailed = clientUsernameResolveFailed;
    }
}
