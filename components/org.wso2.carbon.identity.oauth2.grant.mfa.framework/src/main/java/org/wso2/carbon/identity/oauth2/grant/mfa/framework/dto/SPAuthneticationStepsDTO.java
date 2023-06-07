package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

import java.util.ArrayList;

public class SPAuthneticationStepsDTO {

    private ArrayList<AuthenticationStepDetailsDTO> authenticationStepContextList;

    public void setAuthenticationStepContextList
            (ArrayList<AuthenticationStepDetailsDTO> authenticationStepContextList) {

        this.authenticationStepContextList = authenticationStepContextList;
    }

    public ArrayList<AuthenticationStepDetailsDTO> getAuthenticationStepContextList() {

        return this.authenticationStepContextList;
    }
}
