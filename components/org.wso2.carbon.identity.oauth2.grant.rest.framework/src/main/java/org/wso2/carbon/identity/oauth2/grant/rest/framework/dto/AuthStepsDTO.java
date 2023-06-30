package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AuthStepsDTO {

    private ArrayList<AuthenticationStepDetailsDTO> authenticationStepList;

    public void setAuthenticationStepList
            (LinkedHashMap<Integer, List<String>> authenticationStepList) {

        this.authenticationStepList = authenticationStepList;
    }

    public ArrayList<AuthenticationStepDetailsDTO> getAuthenticationStepList() {

        return this.authenticationStepList;
    }
}
