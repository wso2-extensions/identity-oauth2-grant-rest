package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorConfig;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthnStepConfig;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthStepConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticationStepsResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticatorConfigDTO;

import java.util.ArrayList;
import java.util.List;

public class RestAuthenticationResponseBuilder {

    public static void buildPostAuthenticationResponse() {

    }

    public static List<AuthnStepConfig> buildAuthenticationStepsResponse(AuthenticationStepsResponseDTO responseDTO) {

        List<AuthStepConfigsDTO> authenticationSteps = responseDTO.getAuthenticationSteps(); //framework
        List<AuthnStepConfig> authenticationStepsList = new ArrayList<>();

        AuthnStepConfig authnStepConfig;
        AuthenticatorConfig authenticatorConfigs;
        List<AuthenticatorConfig> authenticatorConfigsList;

        for (AuthStepConfigsDTO authStepConfigsDTO : authenticationSteps) {

            authnStepConfig = new AuthnStepConfig();
            int stepNo = authStepConfigsDTO.getStepNo();

            authenticatorConfigsList = new ArrayList<>();
            for (AuthenticatorConfigDTO authenticatorConfigDTO : authStepConfigsDTO.getAuthenticatorDetails()) {

                authenticatorConfigs = new AuthenticatorConfig();
                String authenticatorName = authenticatorConfigDTO.getAuthenticatorName();
                authenticatorConfigs.setAuthenticatorName(authenticatorName);
                authenticatorConfigsList.add(authenticatorConfigs);

            }
            authnStepConfig.setStepNo(stepNo);
            authnStepConfig.setAuthenticatorConfigs(authenticatorConfigsList);
            authenticationStepsList.add(authnStepConfig);
        }

        return authenticationStepsList;
    }

}
