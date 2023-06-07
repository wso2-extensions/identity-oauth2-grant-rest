package org.wso2.carbon.identity.oauth2.grant.mfa.granttype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;

/**
 * Util methods for MFA OAuth2 Grant Type.
 */
public class MFAOAuthGrantUtils {

    private static final Log log = LogFactory.getLog(MFAOAuthGrantHandler.class);

    /**
     * Method to handle debug logs.
     *
     * @param debugMessage Debug Description
     */
    public static void handleDebugLogs (String debugMessage) {
        if (log.isDebugEnabled()) {
            log.error(debugMessage);
        }
    }

    /**
     * Method to handle exception.
     *
     * @param errorMessage Error Description
     * @throws IdentityOAuth2Exception
     */
    public static void handleException (String errorMessage) throws IdentityOAuth2Exception {
        if (log.isDebugEnabled()) {
            log.error(errorMessage);
        }
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
        if (log.isDebugEnabled()) {
            log.error(errorMessage, e);
        }
        throw new IdentityOAuth2Exception (errorMessage, e);
    }
}
