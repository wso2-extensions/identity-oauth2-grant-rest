package org.wso2.carbon.identity.oauth2.grant.mfa.granttype;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.validators.AbstractValidator;
import javax.servlet.http.HttpServletRequest;

/**
 * Grant validator for REST OAuth2 Grant.
 * REST OAuth2 Grant request should have the required parameters -
 * grant_type, username and flowId.
 */
public class AuthenticationGrantValidator extends AbstractValidator<HttpServletRequest> {

    public AuthenticationGrantValidator() {

        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(AuthenticationGrantConstants.USERNAME_PARAM_PASSWORD_GRANT);
        requiredParams.add(AuthenticationGrantConstants.FLOW_ID_PARAM_PASSWORD_GRANT);

    }
}
