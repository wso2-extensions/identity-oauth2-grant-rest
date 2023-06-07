package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.MFAApiService;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.*;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.util.EndpointUtils;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.MFAInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.MFAValidationFailureReasonDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.MFAValidationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthClientException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

import javax.ws.rs.core.Response;
public class MFAApiServiceImpl implements MFAApiService {
    private static final Log log = LogFactory.getLog(UserApiServiceImpl.class);

    @Override
    public Response mfaInitializePost(MFAInitializationRequest mfaInitializationRequest) {

        String userId = StringUtils.trim(mfaInitializationRequest.getUserId());
        String authenticator = StringUtils.trim(mfaInitializationRequest.getAuthenticator());
        String mfaToken = StringUtils.trim(mfaInitializationRequest.getMfaToken());

        try {
            MFAInitializationResponseDTO responseDTO = EndpointUtils.getMFAAuthService().initializeMFA(userId,
                    authenticator,mfaToken);
            MFAInitializationResponse response = new MFAInitializationResponse()
                    .mfaToken(responseDTO.getMfaToken())
                    .authenticator(responseDTO.getAuthenticator())
                    .otp(responseDTO.getOtp());
            return Response.ok(response).build();
        } catch (MFAAuthClientException e) {
            return EndpointUtils.handleBadRequestResponse(authenticator, e, log);
        } catch (MFAAuthException e) {
            return EndpointUtils.handleServerErrorResponse(authenticator, e, log);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(authenticator, e, log);
        }
    }


    @Override
    public Response mfaValidatePost(MFAValidationRequest mfaValidationRequest) {

        String userId = StringUtils.trim(mfaValidationRequest.getUserId());
        String mfaToken = StringUtils.trim(mfaValidationRequest.getMfaToken());
        String authenticator = StringUtils.trim(mfaValidationRequest.getAuthenticator());
        String otp = StringUtils.trim(mfaValidationRequest.getOtp());

        try {
            MFAValidationResponseDTO responseDTO = EndpointUtils.getMFAAuthService().validateMFA(userId,
                    mfaToken, authenticator, otp);

            MFAValidationFailureReasonDTO failureReasonDTO = responseDTO.getFailureReason();
            MFAValidationFailureReason failureReason = null;
            if (failureReasonDTO != null) {
                failureReason = new MFAValidationFailureReason()
                        .code(failureReasonDTO.getCode())
                        .message(failureReasonDTO.getMessage())
                        .description(failureReasonDTO.getDescription());
            }
            MFAValidationResponse response = new MFAValidationResponse()
                    .isValidOTP(responseDTO.isValidOTP())
                    .userId(responseDTO.getUserId())
                    .mfaToken(responseDTO.getMfaToken())
                    .isAuthFlowCompleted(responseDTO.isAuthFlowCompleted())
                    .authenticatedSteps(responseDTO.getAuthenticatedSteps())
                    .authenticationSteps(responseDTO.getAuthenticationSteps())
                    .nextStep(responseDTO.getNextStep())
                    .failureReason(failureReason);
            return Response.ok(response).build();
        } catch (MFAAuthClientException e) {
            return EndpointUtils.handleBadRequestResponse(authenticator, e, log);
        } catch (MFAAuthException e) {
            return EndpointUtils.handleServerErrorResponse(authenticator, e, log);
        } catch (Throwable e) {
            return EndpointUtils.handleUnexpectedServerError(authenticator, e, log);
        }
    }
}
