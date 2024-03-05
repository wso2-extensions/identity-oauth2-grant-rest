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

package org.wso2.carbon.identity.oauth2.grant.rest.core.handler;

import org.wso2.carbon.identity.oauth2.grant.rest.core.context.RestAuthenticationContext;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;

/**
 * Interface for generate and validate OTPs.
 */
public interface OTPHandlerService {
    /**
     * This method returns OTP Generation Response.
     *
     * @param authenticatorService      Authenticator Service instance.
     * @param userId                    SCIM Id.
     * @param userTenantDomain          User's tenant domain.
     * @return Object                   OTP generation response.
     * @throws AuthenticationException  If any server or client error occurred.
     */
    Object getAuthenticatorServiceGenerationResponse
    (String authenticatorService, String userId, String userTenantDomain)
            throws AuthenticationException;

    /**
     * This method returns OTP Validation Response.
     *
     * @param authenticatorService          Authenticator Service instance.
     * @param restAuthenticationContext     An object of RestAuthenticationContext.
     * @param otp                           OTP to be validated.
     * @return OTP validation result       OTP validation response.
     * @throws AuthenticationException      If any server or client error occurred.
     */
    Object getAuthenticatorServiceValidationResponse
    (String authenticatorService, RestAuthenticationContext restAuthenticationContext, String otp)
            throws AuthenticationException;
}
