package org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant;

import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String CLIENT_TYPE = "oauth2";

    //Config File related Constants
    public static final String MFA_CONFIG_PATH = CarbonUtils.getCarbonHome() + File.separator + "repository"
            + File.separator + "conf" + File.separator;

    public static final String CONFIG_FILE_NAME = "mfa-auth.properties";

    //Config related Constants
    public static final int DEFAULT_MFA_TOKEN_VALIDITY_PERIOD = 600000; //in milliseconds
    public static final int DEFAULT_MFA_TOKEN_TIMESTAMP_SKEW = 0; //in milliseconds

    public static final String MFA_AUTH_ENABLED = "mfaAuth.enabled";
    public static final String MFA_TOKEN_VALIDITY_PERIOD = "mfaAuth.mfaTokenValidityPeriod";
    public static final String MFA_AUTH_SHOW_FAILURE_REASON = "mfaAuth.showValidationFailureReason";
    public static final String MFA_TOKEN_TIMESTAMP_SKEW = "mfaAuth.timestampSkew";
    public static final String MFA_CUSTOM_LOCAL_AUTHENTICATOR = "mfaAuth.localCustomAuthenticator";

    //Authenticator related Constants
    public static final String AUTHENTICATOR_NAME_BASIC_AUTH = "BasicAuthenticator";
    public static final String AUTHENTICATOR_NAME_IDENTIFIER_FIRST = "IdentifierExecutor";
    public static final String AUTHENTICATOR_NAME_SMSOTP = "SMSOTP";
    public static final String AUTHENTICATOR_NAME_EMAILOTP = "EmailOTP";

    //MFA Token Status related Constants
    public static final String MFA_TOKEN_STATE_ACTIVE = "ACTIVE";
    public static final String MFA_TOKEN_STATE_INACTIVE = "INACTIVE";
    public static final String MFA_TOKEN_STATE_EXPIRED = "EXPIRED";

    //DB Field Names
    public static final String DB_FIELD_USER_ID = "USER_ID";
    public static final String DB_FIELD_USERNAME = "USERNAME";
    public static final String DB_FIELD_MFA_TOKEN_ID = "MFA_TOKEN_ID";
    public static final String DB_FIELD_MFA_TOKEN = "MFA_TOKEN";
    public static final String DB_FIELD_TOKEN_STATE = "TOKEN_STATE";
    public static final String DB_FIELD_TIME_GENERATED = "TIME_GENERATED";
    public static final String DB_FIELD_EXPIRY_TIME = "EXPIRY_TIME";
    public static final String DB_FIELD_IS_AUTH_FLOW_COMPLETED = "IS_AUTH_FLOW_COMPLETED";
    public static final String DB_FIELD_SP_APP_ID = "SP_APP_ID";
    public static final String DB_FIELD_DATA_KEY = "DATA_KEY";
    public static final String DB_FIELD_DATA_VALUE = "DATA_VALUE";
    public static final String DB_FIELD_USER_TENANT_ID = "USER_TENANT_ID";
    public static final String DB_FIELD_SP_TENANT_ID = "SP_TENANT_ID";

    //Password Expiry Constants
    public static final String STATE = "state";
    public static final String LAST_CREDENTIAL_UPDATE_TIMESTAMP_CLAIM =
            "http://wso2.org/claims/identity/lastPasswordUpdateTime";
    public static final String CREATED_CLAIM = "http://wso2.org/claims/created";
    public static final String CREATED_CLAIM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String CREATED_CLAIM_TIMEZONE = "GMT";
    public static final String PASSWORD_CHANGE_EVENT_HANDLER_NAME = "passwordExpiry";
    public static final String PASSWORD_EXPIRY_IN_DAYS_FROM_CONFIG = "passwordExpiry.passwordExpiryInDays";
    public static final int PASSWORD_EXPIRY_IN_DAYS_DEFAULT_VALUE = 30;

    //Authentication Parameters
    public static final String BASIC_AUTH_PARAM_USERNAME = "username";
    public static final String BASIC_AUTH_PARAM_PASSWORD = "password";
    public static final String BASIC_AUTH_PARAM_CLIENT_ID = "clientId";

    public static final String MFA_INITIALIZE_PARAM_USER_ID = "userId";
    public static final String MFA_INITIALIZE_PARAM_AUTHENTICATOR = "authenticator";
    public static final String MFA_INITIALIZE_PARAM_MFA_TOKEN = "mfaToken";

    public static final String MFA_VALIDATE_PARAM_USER_ID = "userId";
    public static final String MFA_VALIDATE_PARAM_AUTHENTICATOR = "authenticator";
    public static final String MFA_VALIDATE_PARAM_MFA_TOKEN = "mfaToken";
    public static final String MFA_VALIDATE_PARAM_OTP = "otp";

    //Event Listener Constants
    public static final String EVENT_PRE_AUTHENTICATION = "EVENT_PRE_AUTHENTICATION";
    public static final String EVENT_POST_AUTHENTICATION = "EVENT_POST_AUTHENTICATION";


    /**
     * MFA Auth Service error codes.
     */
    public enum ErrorMessage {

        // Client error codes.
        CLIENT_MANDATORY_VALIDATION_PARAMETERS_EMPTY("MFA-60001", "Mandatory parameters not found.",
                "Mandatory parameters not found : %s."),
        CLIENT_INVALID_AUTHENTICATOR("MFA-60002", "Invalid authenticator",
                "%s is not present in the current authentication step."),
        CLIENT_INVALID_CLIENT_ID("MFA-60003", "Invalid client Id.",
                "Provided client id does not exist : %s"),
        CLIENT_INCORRECT_USER_CREDENTIALS("MFA-60004", "Incorrect user credentials.",
                "Basic Authentication failed for the user."),
        CLIENT_INACTIVE_MFA_TOKEN("MFA-60005", "Inactive MFA Token", "Provided MFA Token is in inactive state : %s"),
        CLIENT_INVALID_MFA_TOKEN("MFA-60006", "Invalid MFA Token", "Provided MFA Token does not exist : %s"),
        CLIENT_AUTHSTEP_OUT_OF_BOUNDS("MFA-60007", "Auth Step Out Of Bounds", "Required Authentication steps have" +
                " already been completed."),
        CLIENT_AUTHENTICATOR_NOT_SUPPORTED("MFA-60008", "Unsupported Authenticator", "Provided Authenticator is not " +
                "supported by MFA Authentication Service: %s"),
        CLIENT_EXPIRED_MFA_TOKEN("MFA-60009", "Expired MFA Token.", "Provided MFA Token is expired : %s"),
        CLIENT_LOCKED_ACCOUNT("MFA-60010", "Locked Account.", "User Account is Locked for the user : %s"),
        CLIENT_DISABLED_ACCOUNT("MFA-60011", "Disabled Account.", "User Account is Disabled for the user : %s"),
        CLIENT_EXPIRED_USER_PASSWORD("MFA-60012", "Expired User Password.", "User Password has Expired for the user :" +
                " %s"),
        CLIENT_USERID_TOKEN_MISMATCH("MFA-60013", "Incorrect User Id.", "User Id is incorrect for the provided MFA " +
                "Token : %s"),
        CLIENT_INVALID_USER("MFA-60014", "Incorrect User Credentials.", "User does not exist : %s"),
        CLIENT_USER_SP_TENANT_MISMATCH("MFA-60015", "IsSaasApp is disabled.", "Cross tenant access is restricted."),
        CLIENT_CUSTOM_AUTHENTICATE_USER_ERROR("MFA-60016", "user authentication error.","%s"),

        // Server error codes.
        SERVER_RETRIEVING_SP_ERROR("MFA-65001", "Service provider error.",
                "Error retrieving service provider."),
        SERVER_AUTHENTICATE_USER_ERROR("MFA-65002", "user authentication error.",
                "Error validating user credentials for the user : %s."),
        SERVER_USER_STORE_MANAGER_ERROR ("MFA-65003", "user store manager error.", "Error retrieving user store " +
                "manager."),
        SERVER_RETRIEVING_USER_ID_ERROR ("MFA-65004", "user store manager error.", "Error retrieving userId for" +
                " the username : %s."),
        SERVER_RETRIEVING_USER_NAME_ERROR ("MFA-65005", "user store manager error.", "Error retrieving username for" +
                " the userId : %s."),
        SERVER_INVALID_APP_ID("MFA-60006", "Service Provider App ID is invalid.",
                "Service Provider App ID is invalid. App ID: %s"),
        SERVER_ACCOUNT_STATUS_ERROR("MFA-60007", "User Account Error.",
                "Error while checking the account status for the user : %s."),
        SERVER_UNEXPECTED_ERROR("MFA-65008", "An unexpected server error occurred.",
                "An unexpected server error occurred."),
        SERVER_CONFIG_FILE_NOT_FOUND_ERROR("MFA-65009", "Config file could not be found.",
                "Config file could not be found: %s"),
        SERVER_REQUEST_PARAM_READING_ERROR("MFA-65010", "Error sanitizing request parameters.",
                "Error while sanitizing the request parameter : %s."),
        SERVER_PROPERTY_NOT_DEFINED_ERROR("MFA-65011", "Property not found.", "Property is not defined in " +
                "configuration file."),
        SERVER_CONFIG_LOADING_IO_ERROR("MFA-65012", "Error while loading MFA Auth configs.",
                "Error while loading MFA Auth configs."),
        SERVER_CONFIG_FILE_CLOSURE_IO_ERROR("MFA-65013", "Failed to close the FileInputStream.",
                "Failed to close the FileInputStream for file %s."),
        SERVER_AUTHENTICATOR_SERVICE_ERROR("MFA-65014", "Failed to retrieve Authenticator Service.",
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

    // Forbidden error codes.
    private static List<String> forbiddenErrors = new ArrayList<>();
    // Conflict error codes.
    private static List<String> conflictErrors = new ArrayList<>();
    // Not Found error codes.
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
