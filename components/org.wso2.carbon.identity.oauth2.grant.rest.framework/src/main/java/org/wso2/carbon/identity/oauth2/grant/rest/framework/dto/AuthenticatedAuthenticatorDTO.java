package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

/**
 * This DTO class is used to keep the data for authenticated authenticators with relevant
 * step numbers in previous authentication steps.
 */
public class AuthenticatedAuthenticatorDTO {

    private Integer stepNo;
    private String authenticatorName;

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public String getAuthenticatorName() {
        return authenticatorName;
    }

    public void setAuthenticatorName(String authenticatorName) {
        this.authenticatorName = authenticatorName;
    }
}
