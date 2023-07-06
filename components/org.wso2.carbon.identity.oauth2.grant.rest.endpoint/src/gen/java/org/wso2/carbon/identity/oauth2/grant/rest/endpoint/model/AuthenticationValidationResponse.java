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

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatedAuthenticator;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticationFailureReason;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthnStepConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AuthenticationValidationResponse  {
  
    private Boolean isStepSuccess;
    private String flowId;
    private Boolean isAuthFlowCompleted;
    private List<AuthenticatedAuthenticator> authenticatedSteps = new ArrayList<>();

    private List<AuthnStepConfig> authenticationSteps = new ArrayList<>();

    private Integer nextStep;
    private AuthenticationFailureReason failureReason;

    /**
    * OTP validated successfully
    **/
    public AuthenticationValidationResponse isStepSuccess(Boolean isStepSuccess) {

        this.isStepSuccess = isStepSuccess;
        return this;
    }
    
    @ApiModelProperty(value = "OTP validated successfully")
    @JsonProperty("isStepSuccess")
    @Valid
    public Boolean getIsStepSuccess() {
        return isStepSuccess;
    }
    public void setIsStepSuccess(Boolean isStepSuccess) {
        this.isStepSuccess = isStepSuccess;
    }

    /**
    * A refreshed token that can be used to uniquely identify the transaction.
    **/
    public AuthenticationValidationResponse flowId(String flowId) {

        this.flowId = flowId;
        return this;
    }
    
    @ApiModelProperty(example = "df09d285-f86f-465b-9959-005841e549c5", required = true, value = "A refreshed token that can be used to uniquely identify the transaction.")
    @JsonProperty("flowId")
    @Valid
    @NotNull(message = "Property flowId cannot be null.")

    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
    * Multi Factor Authentication Status
    **/
    public AuthenticationValidationResponse isAuthFlowCompleted(Boolean isAuthFlowCompleted) {

        this.isAuthFlowCompleted = isAuthFlowCompleted;
        return this;
    }
    
    @ApiModelProperty(example = "false", required = true, value = "Multi Factor Authentication Status")
    @JsonProperty("isAuthFlowCompleted")
    @Valid
    @NotNull(message = "Property isAuthFlowCompleted cannot be null.")

    public Boolean getIsAuthFlowCompleted() {
        return isAuthFlowCompleted;
    }
    public void setIsAuthFlowCompleted(Boolean isAuthFlowCompleted) {
        this.isAuthFlowCompleted = isAuthFlowCompleted;
    }

    /**
    **/
    public AuthenticationValidationResponse authenticatedSteps(List<AuthenticatedAuthenticator> authenticatedSteps) {

        this.authenticatedSteps = authenticatedSteps;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("authenticatedSteps")
    @Valid
    @NotNull(message = "Property authenticatedSteps cannot be null.")

    public List<AuthenticatedAuthenticator> getAuthenticatedSteps() {
        return authenticatedSteps;
    }
    public void setAuthenticatedSteps(List<AuthenticatedAuthenticator> authenticatedSteps) {
        this.authenticatedSteps = authenticatedSteps;
    }

    public AuthenticationValidationResponse addAuthenticatedStepsItem(AuthenticatedAuthenticator authenticatedStepsItem) {
        this.authenticatedSteps.add(authenticatedStepsItem);
        return this;
    }

        /**
    **/
    public AuthenticationValidationResponse authenticationSteps(List<AuthnStepConfig> authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("authenticationSteps")
    @Valid
    @NotNull(message = "Property authenticationSteps cannot be null.")

    public List<AuthnStepConfig> getAuthenticationSteps() {
        return authenticationSteps;
    }
    public void setAuthenticationSteps(List<AuthnStepConfig> authenticationSteps) {
        this.authenticationSteps = authenticationSteps;
    }

    public AuthenticationValidationResponse addAuthenticationStepsItem(AuthnStepConfig authenticationStepsItem) {
        this.authenticationSteps.add(authenticationStepsItem);
        return this;
    }

        /**
    **/
    public AuthenticationValidationResponse nextStep(Integer nextStep) {

        this.nextStep = nextStep;
        return this;
    }
    
    @ApiModelProperty(example = "3", value = "")
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
    public AuthenticationValidationResponse failureReason(AuthenticationFailureReason failureReason) {

        this.failureReason = failureReason;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("failureReason")
    @Valid
    public AuthenticationFailureReason getFailureReason() {
        return failureReason;
    }
    public void setFailureReason(AuthenticationFailureReason failureReason) {
        this.failureReason = failureReason;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationValidationResponse authenticationValidationResponse = (AuthenticationValidationResponse) o;
        return Objects.equals(this.isStepSuccess, authenticationValidationResponse.isStepSuccess) &&
            Objects.equals(this.flowId, authenticationValidationResponse.flowId) &&
            Objects.equals(this.isAuthFlowCompleted, authenticationValidationResponse.isAuthFlowCompleted) &&
            Objects.equals(this.authenticatedSteps, authenticationValidationResponse.authenticatedSteps) &&
            Objects.equals(this.authenticationSteps, authenticationValidationResponse.authenticationSteps) &&
            Objects.equals(this.nextStep, authenticationValidationResponse.nextStep) &&
            Objects.equals(this.failureReason, authenticationValidationResponse.failureReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isStepSuccess, flowId, isAuthFlowCompleted, authenticatedSteps, authenticationSteps, nextStep, failureReason);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationValidationResponse {\n");
        
        sb.append("    isStepSuccess: ").append(toIndentedString(isStepSuccess)).append("\n");
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
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

