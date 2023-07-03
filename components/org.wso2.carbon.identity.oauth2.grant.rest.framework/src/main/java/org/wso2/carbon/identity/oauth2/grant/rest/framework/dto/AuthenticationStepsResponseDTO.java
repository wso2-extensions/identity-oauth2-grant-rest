package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationStepsResponseDTO {

    private List<AuthenticationStepDetailsDTO> authenticationSteps = new ArrayList<>();

    public List<AuthenticationStepDetailsDTO> getAuthenticationSteps() {
        return authenticationSteps;
    }

    public void setAuthenticationSteps(List<AuthenticationStepDetailsDTO> authenticationSteps) {
        this.authenticationSteps = authenticationSteps;
    }
}
