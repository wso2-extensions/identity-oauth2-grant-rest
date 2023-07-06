package org.wso2.carbon.identity.oauth2.grant.rest.handler;

/**
 * Constants used in REST OAuth2 Grant Type.
 */
public class AuthenticationGrantConstants {

    public static final String FLOW_ID_PARAM_PASSWORD_GRANT = "flowId";
    public static final String USERNAME_PARAM_PASSWORD_GRANT = "username";
    public static final String REST_AUTH_GRANT_NAME = "rest_auth_grant";
    public static final String GRANT_TYPE_KEY = "grant_type";

    public static class ErrorMessage {
        public static final String ERROR_UNSUPPORTED_GRANT_TYPE = "Unsupported grant_type value";
        public static final String ERROR_FLOW_ID_RETRIEVING = "Error while retrieving FlowId data";
        public static final String ERROR_VALIDATING_USER = "Error while validating the User";
        public static final String ERROR_UPDATING_FLOW_ID_STATE = "Error while updating Flow Id state";
        public static final String ERROR_INCOMPLETED_AUTHENTICATION_STEPS = "The user has not completed the Required " +
                "Authentication steps";
        public static final String ERROR_INVALID_FLOW_ID = "Invalid Flow Id.";
        public static final String ERROR_VALIDATING_FLOW_ID = "Error while validating the Flow Id";
        public static final String ERROR_FLOW_ID_NULL = "Provided flowId contains a null value";
    }
}
