package org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

public interface AuthenticationStepExecutorService {
    /**
     * This method returns OTP Generation Response.
     *
     * @param authenticatorService Authenticator Service instance.
     * @param userId        SCIM Id.
     * @return OTP generation response.
     * @throws MFAAuthException if any server or client error occurred.
     */
    public Object getAuthenticatorServiceGenerationResponse
            (Object authenticatorService, String userId, String userTenantDomain)
            throws MFAAuthException;

    /**
     * This method returns OTP Validation Response.
     *
     * @param authenticatorService Authenticator Service instance.
     * @param mfaToken      UUID to track the flow.
     * @param userId        SCIM Id.
     * @param otp           OTP to be validated.
     * @return OTP validation result.
     * @throws MFAAuthException if any server or client error occurred.
     */
    public Object getAuthenticatorServiceValidationResponse
            (Object authenticatorService, String mfaToken, String userId,
             String userTenantDomain, String otp) throws MFAAuthException;
}
