package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

public class AuthenticationFlowValidationRequest {

    private String authenticator;
    private String flowId;
    private String password;

    public String getAuthenticator() {

        return authenticator;
    }

    public void setAuthenticator(String authenticator) {

        this.authenticator = authenticator;
    }

    public String getFlowId() {

        return flowId;
    }

    public void setFlowId(String flowId) {

        this.flowId = flowId;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
