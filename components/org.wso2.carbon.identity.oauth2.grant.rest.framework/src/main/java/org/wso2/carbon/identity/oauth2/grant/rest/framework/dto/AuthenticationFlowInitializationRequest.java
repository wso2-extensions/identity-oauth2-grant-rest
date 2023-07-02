package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

public class AuthenticationFlowInitializationRequest {

    private String authenticator;
    private String clientId;
    private String userIdentifier;
    private String password;

    public String getAuthenticator() {

        return authenticator;
    }

    public void setAuthenticator(String authenticator) {

        this.authenticator = authenticator;
    }

    public String getClientId() {

        return clientId;
    }

    public void setClientId(String clientId) {

        this.clientId = clientId;
    }

    public String getUserIdentifier() {

        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {

        this.userIdentifier = userIdentifier;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

}
