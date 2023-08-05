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
