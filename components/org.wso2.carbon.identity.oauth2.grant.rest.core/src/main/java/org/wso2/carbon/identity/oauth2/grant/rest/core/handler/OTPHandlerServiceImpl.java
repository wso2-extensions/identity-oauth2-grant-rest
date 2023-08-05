/*
 *  Copyright (c) 2023, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC licenses this file to you under the Apache license,
 *  Version 2.0 (the "license"); you may not use this file except
 *  in compliance with the license.
 *  You may obtain a copy of the license at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.oauth2.grant.rest.core.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.extension.identity.emailotp.common.exception.EmailOtpException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.core.context.RestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationClientException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.util.RestAuthUtil;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.identity.smsotp.common.exception.SMSOTPException;

/**
 * This class is used to generate and validate OTPs.
 */
public class OTPHandlerServiceImpl implements
        OTPHandlerService {

    private static final Log LOG = LogFactory.getLog(OTPHandlerServiceImpl.class);

    private static final String EmailOtpServiceClass = "org.wso2.carbon.extension.identity.emailotp.common"
            + ".EmailOtpServiceImpl";

    private static final String SMSOtpServiceClass = "org.wso2.carbon.identity.smsotp.common.SMSOTPServiceImpl";

    @Override
    public Object getAuthenticatorServiceGenerationResponse
            (String authenticatorService, String userId, String userTenantDomain) throws AuthenticationException {

        Object authResponseDTO = null;
        String channelName = null;

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(userTenantDomain, true);

            if (SMSOtpServiceClass.equals(authenticatorService)) {
                channelName = Constants.AUTHENTICATOR_NAME_SMSOTP;
                authResponseDTO = ((SMSOTPService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                        getOSGiService(SMSOTPService.class, null)).generateSMSOTP(userId);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SMS OTP generated");
                }

            } else if (EmailOtpServiceClass.equals(authenticatorService)) {
                channelName = Constants.AUTHENTICATOR_NAME_EMAILOTP;
                authResponseDTO = ((EmailOtpService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                        getOSGiService(EmailOtpService.class, null)).generateEmailOTP(userId);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Email OTP generated");
                }
            } else if (authenticatorService == null) {
                throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATOR_SERVICE_ERROR,
                        String.format("Authenticator Service Object is null."));
            }
        } catch (EmailOtpException | SMSOTPException e) {
            LOG.error("OTP generation failed in : " + channelName);
            LOG.error(e);
            throw new AuthenticationClientException(e.getErrorCode(), e.getMessage(), e.getCause());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return authResponseDTO;
    }

    @Override
    public Object getAuthenticatorServiceValidationResponse
            (String authenticatorService, RestAuthenticationContext restAuthenticationContext, String otp)
            throws AuthenticationException {

        Object authResponseDTO = null;
        String channelName = null;

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain
                    (restAuthenticationContext.getUser().getTenantDomain(), true);
            if (SMSOtpServiceClass.equals(authenticatorService)) {
                channelName = Constants.AUTHENTICATOR_NAME_SMSOTP;
                authResponseDTO = ((SMSOTPService) PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService
                        (SMSOTPService.class, null)).validateSMSOTP(restAuthenticationContext.getFlowId(),
                        restAuthenticationContext.getUserId(), otp);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SMS OTP validated");
                }
            } else if (EmailOtpServiceClass.equals(authenticatorService)) {
                channelName = Constants.AUTHENTICATOR_NAME_EMAILOTP;
                authResponseDTO = ((EmailOtpService) PrivilegedCarbonContext.getThreadLocalCarbonContext().
                        getOSGiService(EmailOtpService.class, null)).validateEmailOTP
                        (restAuthenticationContext.getFlowId(), restAuthenticationContext.getUserId(), otp);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Email OTP validated");
                }
            } else if (authenticatorService == null) {
                throw RestAuthUtil.handleServerException(Constants.ErrorMessage.SERVER_AUTHENTICATOR_SERVICE_ERROR,
                        String.format("Authenticator Service Object is null."));
            }
        } catch (EmailOtpException | SMSOTPException e) {
            LOG.error("OTP validation failed in : " + channelName);
            throw new AuthenticationClientException(e.getErrorCode(), e.getMessage(), e.getCause());
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return authResponseDTO;
    }
}
