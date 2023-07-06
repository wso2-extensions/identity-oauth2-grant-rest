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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.handler;

import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;

/**
 * Interface for generate and validate OTPs.
 */
public interface AuthenticationStepExecutorService {
    /**
     * This method returns OTP Generation Response.
     *
     * @param authenticatorService Authenticator Service instance.
     * @param userId        SCIM Id.
     * @return OTP generation response.
     * @throws AuthenticationException if any server or client error occurred.
     */
    Object getAuthenticatorServiceGenerationResponse
            (Object authenticatorService, String userId, String userTenantDomain)
            throws AuthenticationException;

    /**
     * This method returns OTP Validation Response.
     *
     * @param authenticatorService Authenticator Service instance.
     * @param flowId      UUID to track the flow.
     * @param userId        SCIM Id.
     * @param otp           OTP to be validated.
     * @return OTP validation result.
     * @throws AuthenticationException if any server or client error occurred.
     */
    Object getAuthenticatorServiceValidationResponse
            (Object authenticatorService, String flowId, String userId,
             String userTenantDomain, String otp) throws AuthenticationException;
}
