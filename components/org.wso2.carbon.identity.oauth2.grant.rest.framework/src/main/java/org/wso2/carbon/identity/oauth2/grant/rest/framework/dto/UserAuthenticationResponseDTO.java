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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to keep the response data after user is being authenticated.
 */
public class UserAuthenticationResponseDTO {

    private String flowId;
    private boolean isValidPassword;
    private boolean isAuthFlowCompleted;
    private List<AuthenticatedAuthenticatorDTO> authenticatedSteps = new ArrayList<>();
    private List<AuthStepConfigsDTO> authenticationSteps = new ArrayList<>();
    private int nextStep;
    private AuthenticationFailureReasonDTO failureReason;
    public boolean isValidPassword() {

        return isValidPassword;
    }

    public UserAuthenticationResponseDTO setValid(boolean isValid) {
        this.isValidPassword = isValid;
        return this;
    }

    public String getFlowId() {

        return flowId;
    }

    public UserAuthenticationResponseDTO setFlowId(String flowId) {

        this.flowId = flowId;
        return this;
    }

    public boolean isAuthFlowCompleted() {

        return isAuthFlowCompleted;
    }

    public UserAuthenticationResponseDTO setAuthFlowCompleted(boolean authFlowCompleted) {

        this.isAuthFlowCompleted = authFlowCompleted;
        return this;
    }

    public List<AuthenticatedAuthenticatorDTO> getAuthenticatedSteps() {

        return authenticatedSteps;
    }

    public UserAuthenticationResponseDTO setAuthenticatedSteps(List<AuthenticatedAuthenticatorDTO> authenticatedSteps) {

        this.authenticatedSteps = authenticatedSteps;
        return this;
    }

    public List<AuthStepConfigsDTO> getAuthenticationSteps() {

        return authenticationSteps;
    }

    public UserAuthenticationResponseDTO setAuthenticationSteps(List<AuthStepConfigsDTO> authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
        return this;
    }

    public int getNextStep() {

        return nextStep;
    }

    public UserAuthenticationResponseDTO setNextStep(int nextStep) {

        this.nextStep = nextStep;
        return this;
    }

    public AuthenticationFailureReasonDTO getFailureReason() {

        return failureReason;
    }

    public UserAuthenticationResponseDTO setFailureReason(AuthenticationFailureReasonDTO failureReason) {

        this.failureReason = failureReason;
        return this;
    }

}
