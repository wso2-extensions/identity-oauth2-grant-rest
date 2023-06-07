package org.wso2.carbon.identity.oauth2.grant.mfa.granttype;

import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.ResponseHeader;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.CacheBackedMFATokenDAO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.MFATokenDO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.model.RequestParameter;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.token.handlers.grant.PasswordGrantHandler;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

/**
 * Grant Handler for MFA OAuth2 Grant.
 */
public class MFAOAuthGrantHandler extends PasswordGrantHandler {

    /**
     * Validate and Execute the MFA OAuth2 Grant.
     *
     * @param oAuthTokenReqMessageContext   OAuthTokenReqMessageContext
     * @return true or false if the grant_type is valid or not.
     * @throws IdentityOAuth2Exception      Error when validating the User Migration Grant
     */
    @Override
    public boolean validateGrant(OAuthTokenReqMessageContext oAuthTokenReqMessageContext)
            throws IdentityOAuth2Exception {

        MFAOAuthGrantUtils.handleDebugLogs("MFA OAuth2 Grant handler is hit");

        String[] fetchedParams = fetchOauthParameters(oAuthTokenReqMessageContext);

        String username = fetchedParams[0];
        String mfaToken = fetchedParams[1];

        if (mfaToken != null) {

            MFATokenDO tokenBean = null;
            MFAAuthServiceImpl authServiceInstance = new MFAAuthServiceImpl();

            try {
                tokenBean = CacheBackedMFATokenDAO.getInstance().getMFATokenData(mfaToken);
            } catch (MFAAuthException e) {
                MFAOAuthGrantUtils.handleException("Error while retrieving MFA token data", e);
            }

            AuthenticatedUser user = OAuth2Util.getUserFromUserName(username);
            boolean isValidUser = false;

            try {
                isValidUser = tokenBean.getUserId().equals(authServiceInstance.getUserIDFromUserName(username,
                        IdentityTenantUtil.getTenantId(user.getTenantDomain())));
            } catch (MFAAuthException e) {
                MFAOAuthGrantUtils.handleException("Error while validating the User", e);
            }

            oAuthTokenReqMessageContext.setAuthorizedUser(user);
            oAuthTokenReqMessageContext
                    .setScope(oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO().getScope());

            try {
                if (authServiceInstance.isValidMfaToken(tokenBean) && (isValidUser)){
                   if (tokenBean.isAuthFlowCompleted()==true){
                       try {
                           CacheBackedMFATokenDAO.getInstance().updateMfaTokenState(mfaToken,
                                   Constants.MFA_TOKEN_STATE_INACTIVE);
                       } catch (MFAAuthException e) {
                           MFAOAuthGrantUtils.handleException("Error while updating MFA token state", e);
                       }
                       return true;
                   } else {
                       MFAOAuthGrantUtils.handleException("The user has not completed the Required MFA steps");
                   }
                } else {
                    MFAOAuthGrantUtils.handleException("Invalid MFA Token");
                }
            } catch (MFAAuthException e) {
                MFAOAuthGrantUtils.handleException("Error while validating the MFA Token", e);
            }

        } else {
            MFAOAuthGrantUtils.handleException("Provided mfa_token contains a null value");
        }

        responseBuilder(oAuthTokenReqMessageContext);
        return false;
    }

    /**
     * Fetch parameters from authentication request.
     *
     * @param oAuthTokenReqMessageContext   OAuthTokenReqMessageContext
     * @return the parameter values fetched from the request
     */
    public String[] fetchOauthParameters(OAuthTokenReqMessageContext oAuthTokenReqMessageContext) {

        String[] params= new String[2];

        RequestParameter[] parameters =
                oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO().getRequestParameters();

        for (RequestParameter parameter : parameters) {
            if (MFAOAuthGrantConstants.USERNAME_PARAM_PASSWORD_GRANT.equals(parameter.getKey())) {
                if (parameter.getValue() != null && parameter.getValue().length > 0) {
                    params[0] = parameter.getValue()[0];
                }
            }
            if (MFAOAuthGrantConstants.MFA_TOKEN_PARAM_PASSWORD_GRANT.equals(parameter.getKey())) {
                if (parameter.getValue() != null && parameter.getValue().length > 0) {
                    params[1] = parameter.getValue()[0];
                }
            }
        }

        return params;
    }

    /**
     * Build the response header in case the user authentication fails both locally and externally.
     *
     * @param oAuthTokenReqMessageContext    OAuthTokenReqMessageContext
     */
    public void responseBuilder(OAuthTokenReqMessageContext oAuthTokenReqMessageContext) {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setKey("HTTP_STATUS_CODE");
        responseHeader.setValue("401");
        responseHeader.setKey("ERROR_MESSAGE");
        responseHeader.setValue("Unauthorized");
        oAuthTokenReqMessageContext.addProperty("RESPONSE_HEADERS", new ResponseHeader[]{responseHeader});
    }

    @Override
    public boolean authorizeAccessDelegation(OAuthTokenReqMessageContext tokReqMsgCtx) throws IdentityOAuth2Exception {
        return true;
    }

    @Override
    public boolean validateScope(OAuthTokenReqMessageContext toReqMsgCtx) throws IdentityOAuth2Exception {
        return true;
    }

}
