package org.wso2.carbon.identity.oauth2.grant.mfa.framework;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.BasicAuthResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.MFAInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.MFAValidationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

/**
 * MFA Authentication service interface.
 */
public interface MFAAuthService {

    /**
     * This method authenticates a user using credentials.
     *
     * @param username        Username of the user.
     * @param password        Password of the user.
     * @param clientId        Client Id of the Service Provider.
     * @return Basic Authentication response.
     * @throws MFAAuthException if any server or client error occurred.
     */
    BasicAuthResponseDTO authenticatefirstStep(String username, String password, String clientId, String userTenantDomain)
			throws MFAAuthException;

    /**
     * This method will generate an OTP.
     *
     * @param userId        SCIM Id.
     * @param authenticator Authenticator Name.
     * @param mfaToken      UUID to track the flow.
     * @return OTP generation response.
     * @throws MFAAuthException Thrown if any server or client error occurred.
     */
    MFAInitializationResponseDTO initializeMFA(String userId, String authenticator, String mfaToken)
			throws MFAAuthException;

    /**
     * This method validates a provided OTP.
     *
     * @param userId        SCIM Id.
     * @param mfaToken      UUID to track the flow.
     * @param authenticator Authenticator Name.
     * @param otp       	OTP to be validated.
     * @return OTP validation result.
     * @throws MFAAuthException if any server or client error occurred.
     */
    MFAValidationResponseDTO validateMFA(String userId, String mfaToken, String authenticator, String otp)
			throws MFAAuthException;

}
