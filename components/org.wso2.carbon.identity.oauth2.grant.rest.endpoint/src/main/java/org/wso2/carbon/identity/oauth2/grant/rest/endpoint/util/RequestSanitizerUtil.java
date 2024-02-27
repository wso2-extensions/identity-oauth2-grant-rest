/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.commons.lang3.StringUtils;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationValidationRequest;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorInitializationRequest;

/**
 * This Util class is used to sanitize the user input data from endpoint.
 */
public class RequestSanitizerUtil {

    public static boolean isNotEmpty(String requestValue) {

        return StringUtils.isNotEmpty(requestValue);
    }

    public static boolean isEmpty(String requestValue) {

        return StringUtils.isEmpty(requestValue);
    }

    public static AuthenticationValidationRequest trimAuthenticateRequestPayload
            (AuthenticationValidationRequest authenticationValidationRequest) {

        authenticationValidationRequest.setUserIdentifier
                (StringUtils.trim(authenticationValidationRequest.getUserIdentifier()));
        authenticationValidationRequest.setPassword(StringUtils.trim(authenticationValidationRequest.getPassword()));
        authenticationValidationRequest.setClientId(StringUtils.trim(authenticationValidationRequest.getClientId()));
        authenticationValidationRequest.setFlowId(StringUtils.trim(authenticationValidationRequest.getFlowId()));
        authenticationValidationRequest.setAuthenticator
                (StringUtils.trim(authenticationValidationRequest.getAuthenticator()));

        return authenticationValidationRequest;
    }

    public static AuthenticatorInitializationRequest trimInitializationRequestPayload
            (AuthenticatorInitializationRequest authenticatorInitializationRequest) {

        authenticatorInitializationRequest.setFlowId(StringUtils.trim(authenticatorInitializationRequest.getFlowId()));
        authenticatorInitializationRequest.setAuthenticator
                (StringUtils.trim(authenticatorInitializationRequest.getAuthenticator()));

        return authenticatorInitializationRequest;
    }
}
