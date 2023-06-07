package org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.extension.identity.emailotp.common.exception.EmailOtpException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.identity.smsotp.common.exception.SMSOTPException;

public class AuthenticationStepExecutorServiceImpl implements
        AuthenticationStepExecutorService {

    @Override
    public Object getAuthenticatorServiceGenerationResponse
            (Object authenticatorService, String userId, String userTenantDomain) throws MFAAuthException {

        Object mfaResponseDTO = null;

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(userTenantDomain, true);
            if (authenticatorService instanceof SMSOTPService) {
                mfaResponseDTO = ((SMSOTPService)authenticatorService).generateSMSOTP(userId);
            } else if (authenticatorService instanceof EmailOtpService) {
                mfaResponseDTO = ((EmailOtpService)authenticatorService).generateEmailOTP(userId);
            } else if (authenticatorService == null) {
                throw Util.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATOR_SERVICE_ERROR,
                        String.format("Authenticator Service Object is null."));
            }
        } catch (EmailOtpException | SMSOTPException e) {
            throw new MFAAuthClientException(e.getErrorCode(), e.getMessage(), e.getCause());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return mfaResponseDTO;
    }

    @Override
    public Object getAuthenticatorServiceValidationResponse
            (Object authenticatorService, String mfaToken, String userId, String userTenantDomain, String otp)
            throws MFAAuthException {

        Object mfaResponseDTO = null;

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(userTenantDomain, true);
            if (authenticatorService instanceof SMSOTPService) {
                mfaResponseDTO = ((SMSOTPService)authenticatorService).validateSMSOTP(mfaToken, userId, otp);
            } else if (authenticatorService instanceof EmailOtpService) {
                mfaResponseDTO = ((EmailOtpService)authenticatorService).validateEmailOTP(mfaToken, userId, otp);
            } else if (authenticatorService == null) {
                throw Util.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATOR_SERVICE_ERROR,
                        String.format("Authenticator Service Object is null."));
            }
        } catch (EmailOtpException | SMSOTPException e) {
            throw new MFAAuthClientException(e.getErrorCode(), e.getMessage(), e.getCause());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return mfaResponseDTO;
    }
}
