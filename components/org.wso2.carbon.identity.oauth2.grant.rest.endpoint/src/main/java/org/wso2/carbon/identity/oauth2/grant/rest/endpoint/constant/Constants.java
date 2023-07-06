/*
 *  Copyright (c) 2023, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC licenses this file to you under the Apache license,
 *  Version 2.0 (the "license"); you may not use this file except
 *  in compliance with the license.
 *  You may obtain a copy of the license at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.constant;

import org.wso2.carbon.utils.CarbonUtils;
import java.io.File;

/**
 * This class contains the constant variable used in endpoint component.
 */
public class Constants {
    public static final String CORRELATION_ID_MDC = "Correlation-ID";

    public static final String CONFIG_FILE_PATH = CarbonUtils.getCarbonHome() + File.separator + "repository"
            + File.separator + "conf" + File.separator;

    public static final String CONFIG_FILE_NAME = "rest-auth.properties";

    public static final String CLIENT_MANDATORY_PARAMS_EMPTY = "RestClient.mandatoryParamsEmpty";
    public static final String CLIENT_INVALID_AUTHENTICATOR = "RestClient.invalidAuthenticator";
    public static final String CLIENT_INVALID_CLIENT_ID = "RestClient.invalidClientId";
    public static final String CLIENT_INCORRECT_USER_CREDENTIALS = "RestClient.incorrectUserCredentials";
    public static final String CLIENT_INACTIVE_FLOW_ID = "RestClient.inactiveFlowId";
    public static final String CLIENT_INVALID_FLOW_ID = "RestClient.invalidFlowId";
    public static final String CLIENT_AUTH_STEP_OUT_OF_BOUND = "RestClient.authStepOutOfBound";
    public static final String CLIENT_UNSUPPORTED_AUTHENTICATOR = "RestClient.unSupportedAuthenticator";
    public static final String CLIENT_EXPIRED_FLOW_ID = "RestClient.expiredFlowId";
    public static final String CLIENT_LOCKED_USER_ACCOUNT = "RestClient.lockedUserAccount";
    public static final String CLIENT_DISABLED_USER_ACCOUNT = "RestClient.disabledUserAccount";
    public static final String CLIENT_FLOW_ID_MISMATCH = "RestClient.flowIdMismatch";
    public static final String CLIENT_INVALID_USER_CREDENTIALS = "RestClient.invalidUserCredentials";
    public static final String CLIENT_CROSS_TENANT_ACCESS_RESTRICTION = "RestClient.crossTenantAccessRestriction";
    public static final String CLIENT_USERNAME_RESOLVE_FAIL = "RestClient.usernameResolveFailed";
}
