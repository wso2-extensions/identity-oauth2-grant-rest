package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MFAValidationResponse {

    private Boolean isValidOTP;
    private String userId;
    private String mfaToken;
    private Boolean isAuthFlowCompleted;
    private Object authenticatedSteps;
    private Object authenticationSteps;
    private Integer nextStep;
    private MFAValidationFailureReason failureReason;

    /**
     * OTP validated successfully
     **/
    public MFAValidationResponse isValidOTP(Boolean isValidOTP) {

        this.isValidOTP = isValidOTP;
        return this;
    }

    @ApiModelProperty(value = "OTP validated successfully")
    @JsonProperty("isValidOTP")
    @Valid
    public Boolean getIsValidOTP() {
        return isValidOTP;
    }
    public void setIsValidOTP(Boolean isValidOTP) {
        this.isValidOTP = isValidOTP;
    }

    /**
     * SCIM ID of the user which the token issued and validated
     **/
    public MFAValidationResponse userId(String userId) {

        this.userId = userId;
        return this;
    }

    @ApiModelProperty(example = "8b1cc9c4-b671-448a-a334-5afe838a4a3b", required = true, value = "SCIM ID of the user which the token issued and validated")
    @JsonProperty("userId")
    @Valid
    @NotNull(message = "Property userId cannot be null.")

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * A refreshed token that can be used to uniquely identify the transaction
     **/
    public MFAValidationResponse mfaToken(String mfaToken) {

        this.mfaToken = mfaToken;
        return this;
    }

    @ApiModelProperty(required = true, value = "A refreshed token that can be used to uniquely identify the transaction")
    @JsonProperty("mfa_token")
    @Valid
    @NotNull(message = "Property mfaToken cannot be null.")

    public String getMfaToken() {
        return mfaToken;
    }
    public void setMfaToken(String mfaToken) {
        this.mfaToken = mfaToken;
    }

    /**
     * Multi Factor Authentication Status
     **/
    public MFAValidationResponse isAuthFlowCompleted(Boolean isAuthFlowCompleted) {

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
    public MFAValidationResponse authenticatedSteps(Object authenticatedSteps) {

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
    public MFAValidationResponse authenticationSteps(Object authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
        return this;
    }

    @ApiModelProperty(example = "{\"Step1\":[\"BasicAuthenticator\"],\"Step2\":[\"SMSOTP\",\"TOTP\"],\"Step3\":[\"EmailOTP\"]}", required = true, value = "")
    @JsonProperty("authenticationSteps")
    @Valid
    @NotNull(message = "Property authenticationSteps cannot be null.")

    public Object getAuthenticationSteps() {
        return authenticationSteps;
    }
    public void setAuthenticationSteps(Object authenticationSteps) {
        this.authenticationSteps = authenticationSteps;
    }

    /**
     **/
    public MFAValidationResponse nextStep(Integer nextStep) {

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
    public MFAValidationResponse failureReason(MFAValidationFailureReason failureReason) {

        this.failureReason = failureReason;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("failureReason")
    @Valid
    public MFAValidationFailureReason getFailureReason() {
        return failureReason;
    }
    public void setFailureReason(MFAValidationFailureReason failureReason) {
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
        MFAValidationResponse mfaValidationResponse = (MFAValidationResponse) o;
        return Objects.equals(this.isValidOTP, mfaValidationResponse.isValidOTP) &&
                Objects.equals(this.userId, mfaValidationResponse.userId) &&
                Objects.equals(this.mfaToken, mfaValidationResponse.mfaToken) &&
                Objects.equals(this.isAuthFlowCompleted, mfaValidationResponse.isAuthFlowCompleted) &&
                Objects.equals(this.authenticatedSteps, mfaValidationResponse.authenticatedSteps) &&
                Objects.equals(this.authenticationSteps, mfaValidationResponse.authenticationSteps) &&
                Objects.equals(this.nextStep, mfaValidationResponse.nextStep) &&
                Objects.equals(this.failureReason, mfaValidationResponse.failureReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isValidOTP, userId, mfaToken, isAuthFlowCompleted, authenticatedSteps, authenticationSteps, nextStep, failureReason);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MfaValidationResponse {\n");

        sb.append("    isValidOTP: ").append(toIndentedString(isValidOTP)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    mfaToken: ").append(toIndentedString(mfaToken)).append("\n");
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
