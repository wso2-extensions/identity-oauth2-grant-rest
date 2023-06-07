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

package org.wso2.carbon.identity.oauth2.grant.mfa.framework;

import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationInitializationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.SPAuthneticationStepsDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.UserAuthenticationResponseDTO;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.AuthenticationException;

/**
 * Authentication service interface.
 * Authentication service interface.
 */
public interface AuthenticationAuthService {


    /**
     * This method validates a provided OTP.
     *
     * @param flowId      UUID to track the flow.
     * @param authenticator Authenticator Name.
     * @param otp       	OTP to be validated.
     * @return OTP validation result.
     * @throws AuthenticationException if any server or client error occurred.
     */
    UserAuthenticationResponseDTO authenticateWithFlowId(String flowId, String authenticator, String otp)
			throws AuthenticationException;

    AuthenticationInitializationResponseDTO initializeAuth(String flowId,
                                                           String authenticator) throws AuthenticationException;

    UserAuthenticationResponseDTO authenticateWithClientId
            (String clientId, String authenticator, String password, String userIdentifier, String requestTenantDomain)
            throws AuthenticationException;

    SPAuthneticationStepsDTO getAuthenticationStepsFromSP(String clientId) throws AuthenticationException;

}
