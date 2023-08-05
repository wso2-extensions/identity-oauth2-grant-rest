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
