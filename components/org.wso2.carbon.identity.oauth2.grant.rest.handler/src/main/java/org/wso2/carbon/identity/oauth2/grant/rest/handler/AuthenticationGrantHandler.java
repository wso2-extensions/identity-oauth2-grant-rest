package org.wso2.carbon.identity.oauth2.grant.rest.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.ResponseHeader;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.AuthenticationServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dao.CacheBackedFlowIdDAO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dao.FlowIdDO;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.model.RequestParameter;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.token.handlers.grant.PasswordGrantHandler;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import java.util.HashMap;

/**
 * Grant Handler for REST OAuth2 Grant.
 */
public class AuthenticationGrantHandler extends PasswordGrantHandler {

    private static final Log log = LogFactory.getLog(AuthenticationGrantHandler.class);

    /**
     * Validate and Execute the REST OAuth2 Grant.
     *
     * @param oAuthTokenReqMessageContext   OAuthTokenReqMessageContext
     * @return true or false if the grant_type is valid or not.
     * @throws IdentityOAuth2Exception      Error when validating the User Migration Grant
     */
    @Override
    public boolean validateGrant(OAuthTokenReqMessageContext oAuthTokenReqMessageContext)
            throws IdentityOAuth2Exception {

        AuthenticationGrantUtils.handleDebugLogs("REST OAuth2 Grant handler is hit");

        HashMap<String, String> params = new HashMap<>();
        params = fetchOauthParameters(oAuthTokenReqMessageContext);

        String username = params.get(AuthenticationGrantConstants.USERNAME_PARAM_PASSWORD_GRANT);
        String flowId = params.get(AuthenticationGrantConstants.FLOW_ID_PARAM_PASSWORD_GRANT);

        if (StringUtils.isNotBlank(flowId)) {

            FlowIdDO flowIdDO = null;
            AuthenticationServiceImpl authServiceInstance = new AuthenticationServiceImpl();

            try {
                flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);
            } catch (AuthenticationException e) {
                AuthenticationGrantUtils.handleException("Error while retrieving FlowId data", e);
            }

            AuthenticatedUser user = OAuth2Util.getUserFromUserName(username);
            boolean isValidUser = false;

            try {
                isValidUser = flowIdDO.getUserId().equals(authServiceInstance.getUserIDFromUserName(username,
                        IdentityTenantUtil.getTenantId(user.getTenantDomain())));
            } catch (AuthenticationException e) {
                AuthenticationGrantUtils.handleException("Error while validating the User", e);
            }

            oAuthTokenReqMessageContext.setAuthorizedUser(user);
            oAuthTokenReqMessageContext
                    .setScope(oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO().getScope());

            try {
                if (authServiceInstance.isValidFlowId(flowIdDO) && (isValidUser)) {
                   if (flowIdDO.isAuthFlowCompleted()) {
                       try {
                           CacheBackedFlowIdDAO.getInstance().updateFlowIdState(flowId,
                                   Constants.FLOW_ID_STATE_INACTIVE);
                       } catch (AuthenticationException e) {
                           AuthenticationGrantUtils.handleException("Error while updating Flow Id state", e);
                       }
                       return true;
                   } else {
                       AuthenticationGrantUtils.handleException("The user has not completed the Required REST steps");
                   }
                } else {
                    AuthenticationGrantUtils.handleException("Invalid Flow Id.");
                }
            } catch (AuthenticationException e) {
                AuthenticationGrantUtils.handleException("Error while validating the Flow Id", e);
            }

        } else {
            AuthenticationGrantUtils.handleException("Provided flowId contains a null value");
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
    public HashMap<String, String> fetchOauthParameters(OAuthTokenReqMessageContext oAuthTokenReqMessageContext)
            throws IdentityOAuth2Exception {

        RequestParameter[] parameters =
                oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO().getRequestParameters();

        HashMap<String, String> params = new HashMap<>();

        for (RequestParameter parameter : parameters) {
            if (parameter.getKey().equals(AuthenticationGrantConstants.GRANT_TYPE_KEY)) {
                if (!(AuthenticationGrantConstants.REST_AUTH_GRANT_NAME.equals(parameter.getValue()[0]))) {
                    log.error("Grant type is not supported");
                    throw  new IdentityOAuth2Exception("Unsupported grant_type value");
                }
            }
            params.put(parameter.getKey(), parameter.getValue()[0]);
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
