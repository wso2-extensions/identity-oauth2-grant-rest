package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatedAuthenticator;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorConfig;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthnStepConfig;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthStepConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticatedAuthenticatorDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dto.AuthenticatorConfigDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *This class is used to map the authenticators objects with for authentication steps and authenticated steps defined
 * in framework component.
 */
public class RestAuthenticationResponseBuilder {

    public static List<AuthenticatedAuthenticator> buildPostAuthenticatedAuthenticatorResponse
            (List<AuthenticatedAuthenticatorDTO> authenticatedAuthenticators) {

        AuthenticatedAuthenticator authenticatedAuthenticator;
        List<AuthenticatedAuthenticator> authenticatedAuthenticatorsList = new ArrayList<>();

        for (AuthenticatedAuthenticatorDTO authenticatedAuthenticatorDTO : authenticatedAuthenticators) {

            authenticatedAuthenticator = new AuthenticatedAuthenticator();
            int stepNo = authenticatedAuthenticatorDTO.getStepNo();
            String authenticatorName = authenticatedAuthenticatorDTO.getAuthenticatorName();

            authenticatedAuthenticator.setStepNo(stepNo);
            authenticatedAuthenticator.setAuthenticatorName(authenticatorName);
            authenticatedAuthenticatorsList.add(authenticatedAuthenticator);

        }
        return authenticatedAuthenticatorsList;

    }

    public static List<AuthnStepConfig> buildAuthenticationStepsResponse(List<AuthStepConfigsDTO> authenticationSteps) {

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
