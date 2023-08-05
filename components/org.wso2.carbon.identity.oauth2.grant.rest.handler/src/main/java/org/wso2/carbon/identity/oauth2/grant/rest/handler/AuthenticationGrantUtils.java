package org.wso2.carbon.identity.oauth2.grant.rest.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorStatus;
import org.wso2.carbon.identity.application.authentication.framework.config.model.SequenceConfig;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDO;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.dao.FlowIdDO;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

/**
 * RestAuthUtil methods for REST Grant Type.
 */
public class AuthenticationGrantUtils {

    private static final Log log = LogFactory.getLog(AuthenticationGrantHandler.class);

    /**
     * Method to handle debug logs.
     *
     * @param debugMessage Debug Description
     */
    public static void handleDebugLogs (String debugMessage) {
        if (log.isDebugEnabled()) {
            log.debug(debugMessage);
        }
    }

    /**
     * Method to handle exception.
     *
     * @param errorMessage Error Description
     * @throws IdentityOAuth2Exception
     */
    public static void handleException (String errorMessage) throws IdentityOAuth2Exception {
        log.error(errorMessage);
        throw new IdentityOAuth2Exception (errorMessage);
    }

    /**
     * Method to handle exception.
     *
     * @param errorMessage Error Description
     * @param e            Throwable Object
     * @throws IdentityOAuth2Exception
     */
    public static void handleException (String errorMessage, Throwable e) throws IdentityOAuth2Exception {
        log.error(errorMessage, e);
        throw new IdentityOAuth2Exception (errorMessage, e);
    }

    public static void executeEvent
            (String subscriberName, FlowIdDO flowIdDO, AuthenticatedUser user, OAuthAppDO oauthAppDo) {

        Map<String, Object> eventProperties = new HashMap<>();
        Map<String, Object> eventPropertiesParams = new HashMap<>();
        AuthenticationContext authenticationContext = new AuthenticationContext();

        authenticationContext.setContextIdentifier(flowIdDO.getFlowIdIdentifier());
        authenticationContext.setRelyingParty(oauthAppDo.getApplicationName());
        authenticationContext.setServiceProviderName(oauthAppDo.getApplicationName());

        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setApplicationId(String.valueOf(oauthAppDo.getId()));
        sequenceConfig.setAuthenticatedUser(user);
        authenticationContext.setSequenceConfig(sequenceConfig);

        authenticationContext.setRequestType(AuthenticationGrantConstants.REQUEST_TYPE);
        eventProperties.put(AuthenticationGrantConstants.CONTEXT, authenticationContext);

        if (IdentityEventConstants.EventName.AUTHENTICATION_SUCCESS.toString().equals(subscriberName)) {
            eventProperties.put(Constants.AUTHENTICATION_STATUS, AuthenticatorStatus.PASS);
        } else if (IdentityEventConstants.EventName.AUTHENTICATION_FAILURE.toString().equals(subscriberName)) {
            eventProperties.put(Constants.AUTHENTICATION_STATUS, AuthenticatorStatus.FAIL);
        }

        eventPropertiesParams.put(AuthenticationGrantConstants.USER, user);
        eventPropertiesParams.put(AuthenticationGrantConstants.IS_FEDERATED, false);
        eventProperties.put(AuthenticationGrantConstants.PARAMS, eventPropertiesParams);

        Event event = new Event(subscriberName, eventProperties);

        try {
            ((IdentityEventService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                    getOSGiService(IdentityEventService.class, null)).handleEvent(event);
        } catch (IdentityEventException e) {
            log.error(e.getCause());
        }

    }

    public static String errorFormator(AuthenticationException e) {

        return String.format("erroCode = %s ; errorMessage = %s ; errorDescription = %s", e.getErrorCode(),
                e.getMessage(), e.getDescription());
    }

}
