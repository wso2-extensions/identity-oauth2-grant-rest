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

package org.wso2.carbon.identity.oauth2.grant.rest.handler;

/**
 * Constants used in REST OAuth2 Grant Type.
 */
public class AuthenticationGrantConstants {

    public static final String FLOW_ID_PARAM_PASSWORD_GRANT = "flowId";
    public static final String REST_AUTH_GRANT_NAME = "urn:ietf:params:oauth:grant-type:rest";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String CONTEXT = "context";
    public static final String AUTHENTICATION_STATUS = "authenticationStatus";
    public static final String REQUEST_TYPE = "oidc";
    public static final String USER = "user";
    public static final String IS_FEDERATED = "isFederated";
    public static final String PARAMS = "params";
    public static final String OAUTH_APP_DO = "OAuthAppDO";
    public static final String TENANT_DOMAIN_SPLITTER = "@";

    /**
     * Error response messages.
     */
    public static class ErrorMessage {
        public static final String ERROR_INCOMPLETED_AUTHENTICATION_STEPS = "The user has not completed the Required " +
                "Authentication steps";
        public static final String ERROR_FLOW_ID_NULL = "Provided flowId contains a null value";

    }
}
