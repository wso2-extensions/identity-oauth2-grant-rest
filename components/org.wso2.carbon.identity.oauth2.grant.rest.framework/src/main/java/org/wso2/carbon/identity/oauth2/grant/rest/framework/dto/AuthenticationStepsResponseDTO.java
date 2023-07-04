package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationStepsResponseDTO {

    private List<AuthStepConfigsDTO> authenticationSteps = new ArrayList<>();

    public List<AuthStepConfigsDTO> getAuthenticationSteps() {

        return authenticationSteps;
    }

    public void setAuthenticationSteps(List<AuthStepConfigsDTO> authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
    }
}
