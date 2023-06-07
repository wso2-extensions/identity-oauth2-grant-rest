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

package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationStepDetailsDTO;
import java.util.ArrayList;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserAuthenticationResponse {

    private Boolean isValidPassword;
    private String userId;
    private String flowId;
    private Boolean isAuthFlowCompleted;
    private Object authenticatedSteps;
    private Object authenticationSteps;
    private Integer nextStep;
    private ArrayList<AuthenticationStepDetailsDTO> authenticationStepDetails;
    private AuthenticationValidationFailureReason failureReason;

    /**
     * Password validated successfully.
     **/
    public UserAuthenticationResponse isValidPassword(Boolean isValidPassword) {

        this.isValidPassword = isValidPassword;
        return this;
    }

    @ApiModelProperty(value = "Password validated successfully")
    @JsonProperty("isValidPassword")
    @Valid
    public Boolean getIsValidPassword() {
        return isValidPassword;
    }
    public void setIsValidPassword(Boolean isValidPassword) {
        this.isValidPassword = isValidPassword;
    }

    /**
     * SCIM ID of the user which the Flow Id issued and validated.
     **/
    public UserAuthenticationResponse userId(String userId) {

        this.userId = userId;
        return this;
    }


    /**
     * A refreshed Flow Id that can be used to uniquely identify the transaction.
     **/
    public UserAuthenticationResponse flowId(String flowId) {

        this.flowId = flowId;
        return this;
    }

    @ApiModelProperty(required = true,
            value = "A refreshed Flow Id that can be used to uniquely identify the transaction")
    @JsonProperty("flowId")
    @Valid
    @NotNull(message = "Property Flow Id cannot be null.")

    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * Multi Factor Authentication Status.
     **/
    public UserAuthenticationResponse isAuthFlowCompleted(Boolean isAuthFlowCompleted) {

        this.isAuthFlowCompleted = isAuthFlowCompleted;
        return this;
    }

    @ApiModelProperty(example = "false", required = true, value = "Multi Factor Authentication Status")
    @JsonProperty("isAuthFlowCompleted")
    @Valid
    @NotNull(message = "Property isAuthFlowCompleted cannot be null.")

    public Boolean isAuthFlowCompleted() {
        return isAuthFlowCompleted;
    }
    public void setAuthFlowCompleted(Boolean isAuthFlowCompleted) {
        this.isAuthFlowCompleted = isAuthFlowCompleted;
    }

    /**
     **/
    public UserAuthenticationResponse authenticatedSteps(Object authenticatedSteps) {

        this.authenticatedSteps = authenticatedSteps;
        return this;
    }

    @ApiModelProperty(example = "{\"Step1\":\"BasicAuthenticator\",\"Step2\":\"SMSOTP\"}", required = true, value = "")
    @JsonProperty("authenticatedSteps")
    @Valid
    @NotNull(message = "Property authenticatedSteps cannot be null.")

    public Object getAuthenticatedSteps() {
        return authenticatedSteps;
    }
    public void setAuthenticatedSteps(Object authenticatedSteps) {
        this.authenticatedSteps = authenticatedSteps;
    }

    /**
     **/
    public UserAuthenticationResponse authenticationSteps(Object authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
        return this;
    }

    @ApiModelProperty(
            example = "\"[{\\\"stepNo\\\":1,\\\"authenticator\\\":[{\\\"authenticatorName\\\":\\\"" +
                    "BasicAuthenticator\\\",\\\"isMandatory\\\":true}]},{\\\"stepNo\\\":2,\\\"authenticator\\\"" +
                    ":[{\\\"authenticatorName\\\":\\\"EmailOTP\\\",\\\"isMandatory\\\":true},{\\\"" +
                    "authenticatorName\\\":\\\"SMSOTP\\\",\\\"isMandatory\\\":false}]}]\"")
    @JsonProperty("authenticationSteps")
    @Valid
    public ArrayList<AuthenticationStepDetailsDTO> getAuthenticartorsDetails() {
        return authenticationStepDetails;
    }

    public UserAuthenticationResponse setAutheticatorStepDetails
            (ArrayList<AuthenticationStepDetailsDTO> authenticationStepDetails) {

        this.authenticationStepDetails = authenticationStepDetails;
        return this;
    }
    /**
     **/
    public UserAuthenticationResponse nextStep(Integer nextStep) {

        if (nextStep == -1) {
            this.nextStep = null;
        } else {
            this.nextStep = nextStep;
        }
        return this;
    }

    @ApiModelProperty(example = "Step3", value = "")
    @JsonProperty("nextStep")
    @Valid
    public Integer getNextStep() {
        return nextStep;
    }
    public void setNextStep(Integer nextStep) {
        this.nextStep = nextStep;
    }

    /**
     **/
    public UserAuthenticationResponse failureReason(AuthenticationValidationFailureReason failureReason) {

        this.failureReason = failureReason;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("failureReason")
    @Valid
    public AuthenticationValidationFailureReason getFailureReason() {
        return failureReason;
    }
    public void setFailureReason(AuthenticationValidationFailureReason failureReason) {
        this.failureReason = failureReason;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuthenticationResponse userAuthenticationResponse = (UserAuthenticationResponse) o;
        return Objects.equals(this.isValidPassword, userAuthenticationResponse.isValidPassword) &&
                Objects.equals(this.userId, userAuthenticationResponse.userId) &&
                Objects.equals(this.flowId, userAuthenticationResponse.flowId) &&
                Objects.equals(this.isAuthFlowCompleted, userAuthenticationResponse.isAuthFlowCompleted) &&
                Objects.equals(this.authenticatedSteps, userAuthenticationResponse.authenticatedSteps) &&
                Objects.equals(this.authenticationSteps, userAuthenticationResponse.authenticationSteps) &&
                Objects.equals(this.nextStep, userAuthenticationResponse.nextStep) &&
                Objects.equals(this.failureReason, userAuthenticationResponse.failureReason);
    }

    @Override
    public int hashCode() {

        return Objects.hash(isValidPassword, userId, flowId, isAuthFlowCompleted, authenticatedSteps,
                authenticationSteps, nextStep, failureReason);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationValidationResponse {\n");

        sb.append("    isValidPassword: ").append(toIndentedString(isValidPassword)).append("\n");
        sb.append("    flowId: ").append(toIndentedString(flowId)).append("\n");
        sb.append("    isAuthFlowCompleted: ").append(toIndentedString(isAuthFlowCompleted)).append("\n");
        sb.append("    authenticatedSteps: ").append(toIndentedString(authenticatedSteps)).append("\n");
        sb.append("    authenticationSteps: ").append(toIndentedString(authenticationSteps)).append("\n");
        sb.append("    nextStep: ").append(toIndentedString(nextStep)).append("\n");
        sb.append("    failureReason: ").append(toIndentedString(failureReason)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}
