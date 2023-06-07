package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

public class AuthenticatorDetailsDTO {
    private String authenticatorName;
    private boolean isMandatory;
    public void setAuthenticatorName(String authenticatorName){
        this.authenticatorName = authenticatorName;
    }
    public void setIsMandatory(Boolean isMandatory){
        this.isMandatory = isMandatory;
    }
    public String getAuthenticatorName(){
        return this.authenticatorName;
    }
    public boolean getIsMandatory(){
        return this.isMandatory;
    }
}
