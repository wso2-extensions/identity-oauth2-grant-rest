package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

import java.util.List;

public class AuthStepConfigsDTO {

    private Integer stepNo;
    private List<AuthenticatorConfigDTO> authenticatorConfigs = null;

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public List<AuthenticatorConfigDTO> getAuthenticatorConfigs() {
        return authenticatorConfigs;
    }

    public void setAuthenticatorConfigs(List<AuthenticatorConfigDTO> authenticatorConfigs) {
        this.authenticatorConfigs = authenticatorConfigs;
    }
}
