/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthStepConfigsDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticatedAuthenticatorDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dto.AuthenticatorConfigDTO;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatedAuthenticator;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorConfig;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthnStepConfig;

import java.util.ArrayList;
import java.util.List;

/**
 *This class is used to map the authenticators objects with for authentication steps and authenticated steps defined
 * in core component.
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
