
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
 */

package org.wso2.carbon.identity.oauth2.grant.rest.framework.constant;

import org.wso2.carbon.utils.CarbonUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to keep the constants relevant to framework component.
 */
public class Constants {

    public static final String CLIENT_TYPE = "oauth2";

    //Config File related Constants
    public static final String CONFIG_FILE_PATH = CarbonUtils.getCarbonHome() + File.separator + "repository"
            + File.separator + "conf" + File.separator;

    public static final String CONFIG_FILE_NAME = "rest-auth.properties";

    //Config related Constants
    public static final int DEFAULT_FLOW_ID_VALIDITY_PERIOD = 600000; //in milliseconds
    public static final int DEFAULT_FLOW_ID_TIMESTAMP_SKEW = 0; //in milliseconds

    public static final String REST_AUTH_ENABLED = "RestAuth.enabled";
    public static final String FLOW_ID_VALIDITY_PERIOD = "RestAuth.FlowIdValidityPeriod";
    public static final String AUTH_SHOW_FAILURE_REASON = "RestAuth.showValidationFailureReason";
    public static final String FLOW_ID_TIMESTAMP_SKEW = "RestAuth.timestampSkew";
    public static final String CUSTOM_LOCAL_AUTHENTICATOR = "RestAuth.localCustomAuthenticator";

    //Authenticator related Constants
    public static final String AUTHENTICATOR_NAME_BASIC_AUTH = "BasicAuthenticator";
    public static final String AUTHENTICATOR_NAME_IDENTIFIER_FIRST = "IdentifierExecutor";
    public static final String AUTHENTICATOR_NAME_SMSOTP = "SMSOTP";
    public static final String AUTHENTICATOR_NAME_EMAILOTP = "EmailOTP";

    //Flow Id Status related Constants
    public static final String FLOW_ID_STATE_ACTIVE = "ACTIVE";
    public static final String FLOW_ID_STATE_INACTIVE = "INACTIVE";
    public static final String FLOW_ID_STATE_EXPIRED = "EXPIRED";

    //DB Field Names
    public static final String DB_FIELD_USER_ID = "USER_ID";
    public static final String DB_FIELD_USERNAME = "USERNAME";
    public static final String DB_FIELD_FLOW_ID_IDENTIFIER = "FLOW_ID_IDENTIFIER";
    public static final String DB_FIELD_FLOW_ID = "FLOW_ID";
    public static final String DB_FIELD_FLOW_ID_STATE = "FLOW_ID_STATE";
    public static final String DB_FIELD_TIME_GENERATED = "TIME_GENERATED";
    public static final String DB_FIELD_EXPIRY_TIME = "EXPIRY_TIME";
    public static final String DB_FIELD_IS_AUTH_FLOW_COMPLETED = "IS_AUTH_FLOW_COMPLETED";
    public static final String DB_FIELD_SP_APP_ID = "SP_APP_ID";
    public static final String DB_FIELD_DATA_KEY = "DATA_KEY";
    public static final String DB_FIELD_DATA_VALUE = "DATA_VALUE";
    public static final String DB_FIELD_USER_TENANT_ID = "USER_TENANT_ID";
    public static final String DB_FIELD_SP_TENANT_ID = "SP_TENANT_ID";


    //Authentication Parameters
    public static final String BASIC_AUTH_PARAM_USERNAME = "username";
    public static final String LOGGED_USER_CLAIM = "loggedUserClaim";
    public static final String BASIC_AUTH_PARAM_CLIENT_ID = "clientId";

    public static final String INITIALIZE_PARAM_AUTHENTICATOR = "authenticator";
    public static final String INITIALIZE_PARAM_FLOW_ID = "flowId";

    public static final String VALIDATE_PARAM_AUTHENTICATOR = "authenticator";
    public static final String VALIDATE_PARAM_FLOW_ID = "flowId";
    public static final String VALIDATE_PARAM_PASSWORD = "password";

    //Event Listener Constants
    public static final String PRE_AUTHENTICATION = "PRE_AUTHENTICATION";
    public static final String POST_AUTHENTICATION = "POST_AUTHENTICATION";

    public static final String ACCOUNT_LOCK_ERROR_CODE = "17003";
    public static final String ACCOUNT_DISABLE_ERROR_CODE = "17004";

    /**
     * Rest Auth Service error codes.
     */
    public enum ErrorMessage {

        // Client error codes.
        CLIENT_MANDATORY_VALIDATION_PARAMETERS_EMPTY(
                "E-60001",
                "Mandatory parameters not found.",
                "Mandatory parameters not found : %s."),
        CLIENT_INVALID_AUTHENTICATOR(
                "E-60002",
                "Invalid authenticator",
                "%s is not present in the current authentication step."),
        CLIENT_INVALID_CLIENT_ID(
                "E-60003",
                "Invalid client Id.",
                "Provided client id does not exist : %s"),
        CLIENT_INCORRECT_USER_CREDENTIALS(
                "E-60004",
                "Incorrect user credentials.",
                "Basic Authentication failed for the user."),
        CLIENT_INACTIVE_FLOW_ID(
                "E-60005",
                "Inactive Flow Id",
                "Provided Flow Id is in inactive state : %s"),
        CLIENT_INVALID_FLOW_ID(
                "E-60006",
                "Invalid Flow Id",
                "Provided Flow Id does not exist : %s"),
        CLIENT_AUTHSTEP_OUT_OF_BOUNDS(
                "E-60007",
                "Auth Step Out Of Bounds",
                "Required Authentication steps have already been completed."),
        CLIENT_AUTHENTICATOR_NOT_SUPPORTED(
                "E-60008",
                "Unsupported Authenticator",
                "Provided Authenticator is not supported by E Authentication Service: %s"),
        CLIENT_EXPIRED_FLOW_ID(
                "E-60009",
                "Expired Flow Id.",
                "Provided Flow Id is expired : %s"),
        CLIENT_LOCKED_ACCOUNT(
                "E-60010",
                "Locked Account.",
                "User Account is Locked for the user : %s"),
        CLIENT_DISABLED_ACCOUNT(
                "E-60011",
                "Disabled Account.",
                "User Account is Disabled for the user : %s"),

        CLIENT_USERID_FLOWID_MISMATCH(
                "E-60013",
                "Incorrect User Id.",
                "User Id is incorrect for the provided Flow Id : %s"),
        CLIENT_INVALID_USER(
                "E-60014",
                "Incorrect User Credentials.",
                "User does not exist : %s"),
        CLIENT_USER_SP_TENANT_MISMATCH(
                "E-60015",
                "IsSaasApp is disabled.",
                "Cross tenant access is restricted."),
        CLIENT_CUSTOM_AUTHENTICATE_USER_ERROR(
                "E-60016",
                "user authentication error.",
                "%s"),


        // Server error codes.
        SERVER_RETRIEVING_SP_ERROR(
                "E-65001",
                "Service provider error.",
                "Error retrieving service provider."),
        SERVER_AUTHENTICATE_USER_ERROR(
                "E-65002",
                "user authentication error.",
                "Error validating user credentials for the user : %s."),
        SERVER_USER_STORE_MANAGER_ERROR (
                "E-65003",
                "user store manager error.",
                "Error retrieving user store manager."),
        SERVER_RETRIEVING_USER_ID_ERROR (
                "E-65004",
                "user store manager error.",
                "Error retrieving userId for the username : %s."),
        SERVER_RETRIEVING_USER_NAME_ERROR (
                "E-65005",
                "user store manager error.",
                "Error retrieving username for the userId : %s."),
        SERVER_INVALID_APP_ID(
                "E-60006",
                "Service Provider App ID is invalid.",
                "Service Provider App ID is invalid. App ID: %s"),
        SERVER_ACCOUNT_STATUS_ERROR(
                "E-60007",
                "User Account Error.",
                "Error while checking the account status for the user : %s."),
        SERVER_UNEXPECTED_ERROR(
                "E-65008",
                "An unexpected server error occurred.",
                "An unexpected server error occurred."),
        SERVER_CONFIG_FILE_NOT_FOUND_ERROR(
                "E-65009",
                "Config file could not be found.",
                "Config file could not be found: %s"),
        SERVER_REQUEST_PARAM_READING_ERROR(
                "E-65010",
                "Error sanitizing request parameters.",
                "Error while sanitizing the request parameter : %s."),
        SERVER_PROPERTY_NOT_DEFINED_ERROR(
                "E-65011",
                "Property not found.",
                "Property is not defined in configuration file."),
        SERVER_CONFIG_LOADING_IO_ERROR(
                "E-65012",
                "Error while loading E Auth configs.",
                "Error while loading E Auth configs."),
        SERVER_CONFIG_FILE_CLOSURE_IO_ERROR(
                "E-65013",
                "Failed to close the FileInputStream.",
                "Failed to close the FileInputStream for file %s."),
        SERVER_AUTHENTICATOR_SERVICE_ERROR(
                "E-65014",
                "Failed to retrieve Authenticator Service.",
                "Authenticator Service Object is null.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        public String toString() {

            return getCode() + " | " + message;
        }
    }

    private static List<String> forbiddenErrors = new ArrayList<>();
    private static List<String> conflictErrors = new ArrayList<>();
    private static List<String> notFoundErrors = Arrays.asList(ErrorMessage.CLIENT_INVALID_CLIENT_ID.code);

    public static boolean isForbiddenError(String errorCode) {

        return forbiddenErrors.contains(errorCode);
    }

    public static boolean isConflictError(String errorCode) {

        return conflictErrors.contains(errorCode);
    }

    public static boolean isNotFoundError(String errorCode) {

        return notFoundErrors.contains(errorCode);
    }
}