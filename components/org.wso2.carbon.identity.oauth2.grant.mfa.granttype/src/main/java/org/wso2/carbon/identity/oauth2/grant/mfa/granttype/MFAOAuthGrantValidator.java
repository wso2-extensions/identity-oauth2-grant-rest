package org.wso2.carbon.identity.oauth2.grant.mfa.granttype;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.validators.AbstractValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * Grant validator for MFA OAuth2 Grant.
 * MFA OAuth2 Grant request should have the required parameters -
 * grant_type, username and mfa_token.
 */
public class MFAOAuthGrantValidator extends AbstractValidator<HttpServletRequest> {

    public MFAOAuthGrantValidator() {

        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(MFAOAuthGrantConstants.USERNAME_PARAM_PASSWORD_GRANT);
        requiredParams.add(MFAOAuthGrantConstants.MFA_TOKEN_PARAM_PASSWORD_GRANT);

    }

}
