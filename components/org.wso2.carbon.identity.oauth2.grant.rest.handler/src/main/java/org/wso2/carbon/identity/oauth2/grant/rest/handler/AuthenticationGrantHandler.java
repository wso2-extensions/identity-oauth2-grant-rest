/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.grant.rest.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDO;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.ResponseHeader;
import org.wso2.carbon.identity.oauth2.grant.rest.core.RestAuthenticationServiceImpl;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.CacheBackedFlowIdDAO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.FlowIdDO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.model.RequestParameter;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.token.handlers.grant.AbstractAuthorizationGrantHandler;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import java.util.HashMap;

/**
 * Grant Handler for REST OAuth2 Grant.
 */
public class AuthenticationGrantHandler extends AbstractAuthorizationGrantHandler {

    private static final Log LOG = LogFactory.getLog(AuthenticationGrantHandler.class);

    /**
     * Validate and Execute the REST OAuth2 Grant.
     *
     * @param oAuthTokenReqMessageContext   OAuthTokenReqMessageContext
     * @return true or false if the grant_type is valid or not.
     * @throws IdentityOAuth2Exception      Error when validating the Grant
     */
    @Override
    public boolean validateGrant(OAuthTokenReqMessageContext oAuthTokenReqMessageContext)
            throws IdentityOAuth2Exception {

        AuthenticationGrantUtils.handleDebugLogs("REST OAuth2 Grant handler is hit");

        HashMap<String, String> params = new HashMap<>();
        params = fetchOauthParameters(oAuthTokenReqMessageContext);
        FlowIdDO flowIdDO = null;
        RestAuthenticationServiceImpl authServiceInstance = new RestAuthenticationServiceImpl();
        AuthenticatedUser user = null;

        String flowId = params.get(AuthenticationGrantConstants.FLOW_ID_PARAM_PASSWORD_GRANT);

        try {
            flowIdDO = CacheBackedFlowIdDAO.getInstance().getFlowIdData(flowId);
            user = OAuth2Util.getUserFromUserName(flowIdDO.getFullQualifiedUserName().split
                    (AuthenticationGrantConstants.TENANT_DOMAIN_SPLITTER)[0]);

            if (StringUtils.isNotBlank(flowId) && authServiceInstance.isValidFlowId(flowIdDO)) {

                if (!flowIdDO.isAuthFlowCompleted()) {
                    LOG.error(AuthenticationGrantConstants.ErrorMessage.ERROR_INCOMPLETED_AUTHENTICATION_STEPS);
                    AuthenticationGrantUtils.executeEvent
                            (IdentityEventConstants.EventName.AUTHENTICATION_FAILURE.toString(), flowIdDO, user,
                                    (OAuthAppDO) oAuthTokenReqMessageContext.getProperty
                                            (AuthenticationGrantConstants.OAUTH_APP_DO));
                    AuthenticationGrantUtils.handleException
                            (AuthenticationGrantConstants.ErrorMessage.ERROR_INCOMPLETED_AUTHENTICATION_STEPS);
                }

                oAuthTokenReqMessageContext.setAuthorizedUser(user);
                oAuthTokenReqMessageContext.setScope(oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO()
                        .getScope());

                CacheBackedFlowIdDAO.getInstance().updateFlowIdState(flowId, Constants.FLOW_ID_STATE_INACTIVE);
                AuthenticationGrantUtils.executeEvent
                        (IdentityEventConstants.EventName.AUTHENTICATION_SUCCESS.toString(), flowIdDO, user,
                                (OAuthAppDO) oAuthTokenReqMessageContext.getProperty
                                        (AuthenticationGrantConstants.OAUTH_APP_DO));

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Flow ID updated to Inactive state");
                }
                return true;
            }
        } catch (AuthenticationException e) {
            AuthenticationGrantUtils.errorFormator(e);
            if (flowIdDO != null && flowIdDO.isAuthFlowCompleted()) {
                AuthenticationGrantUtils.executeEvent
                        (IdentityEventConstants.EventName.AUTHENTICATION_FAILURE.toString(), flowIdDO, user,
                                (OAuthAppDO) oAuthTokenReqMessageContext.getProperty
                                        (AuthenticationGrantConstants.OAUTH_APP_DO));
            }
            AuthenticationGrantUtils.handleException(e.getDescription(), e);
        }


        grantValidationFailedResponseBuilder(oAuthTokenReqMessageContext);
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
                    LOG.error("Grant type is not supported");
                    throw  new IdentityOAuth2Exception(AuthenticationGrantConstants.ErrorMessage.ERROR_FLOW_ID_NULL);
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
    public void grantValidationFailedResponseBuilder(OAuthTokenReqMessageContext oAuthTokenReqMessageContext) {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setKey("HTTP_STATUS_CODE");
        responseHeader.setValue("401");
        responseHeader.setKey("ERROR_MESSAGE");
        responseHeader.setValue("Unauthorized");
        oAuthTokenReqMessageContext.addProperty("RESPONSE_HEADERS", new ResponseHeader[]{responseHeader});
    }

}
