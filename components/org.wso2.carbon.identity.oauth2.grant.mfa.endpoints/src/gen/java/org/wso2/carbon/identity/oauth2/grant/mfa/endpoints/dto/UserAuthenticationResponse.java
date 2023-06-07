package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserAuthenticationResponse {

    private String mfaToken;
    private Object authenticatedSteps;
    private Object authenticationSteps;
    private Boolean isAuthFlowCompleted;
    private Integer nextStep;

    /**
     * A token that can be used to uniquely identify the transaction
     **/
    public UserAuthenticationResponse mfaToken(String mfaToken) {

        this.mfaToken = mfaToken;
        return this;
    }

    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ", value = "A token that can be used to uniquely identify the transaction")
    @JsonProperty("mfa_token")
    @Valid
    public String getMfaToken() {
        return mfaToken;
    }
    public void setMfaToken(String mfaToken) {
        this.mfaToken = mfaToken;
    }

    /**
     **/
    public UserAuthenticationResponse authenticatedSteps(Object authenticatedSteps) {

        this.authenticatedSteps = authenticatedSteps;
        return this;
    }

    @ApiModelProperty(example = "{\"Step1\":\"BasicAuthenticator\"}", value = "")
    @JsonProperty("authenticatedSteps")
    @Valid
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

    @ApiModelProperty(example = "{\"Step1\":[\"BasicAuthenticator\"],\"Step2\":[\"SMSOTP\",\"EmailOTP\"],\"Step3\":[\"TOTP\"]}", value = "")
    @JsonProperty("authenticationSteps")
    @Valid
    public Object getAuthenticationSteps() {
        return authenticationSteps;
    }
    public void setAuthenticationSteps(Object authenticationSteps) {

        this.authenticationSteps = authenticationSteps;
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

    @ApiModelProperty(example = "Step2", value = "")
    @JsonProperty("nextStep")
    @Valid
    public Integer getNextStep() {
        return nextStep;
    }
    public void setNextStep(Integer nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * Multi Factor Authentication Status
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


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuthenticationResponse userAuthenticationResponse = (UserAuthenticationResponse) o;
        return Objects.equals(this.mfaToken, userAuthenticationResponse.mfaToken) &&
                Objects.equals(this.authenticatedSteps, userAuthenticationResponse.authenticatedSteps) &&
                Objects.equals(this.authenticationSteps, userAuthenticationResponse.authenticationSteps) &&
                Objects.equals(this.isAuthFlowCompleted, userAuthenticationResponse.isAuthFlowCompleted) &&
                Objects.equals(this.nextStep, userAuthenticationResponse.nextStep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mfaToken, authenticatedSteps, authenticationSteps, isAuthFlowCompleted, nextStep);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserAuthenticationResponse {\n");

        sb.append("    mfaToken: ").append(toIndentedString(mfaToken)).append("\n");
        sb.append("    authenticatedSteps: ").append(toIndentedString(authenticatedSteps)).append("\n");
        sb.append("    authenticationSteps: ").append(toIndentedString(authenticationSteps)).append("\n");
        sb.append("    isAuthFlowCompleted: ").append(toIndentedString(isAuthFlowCompleted)).append("\n");
        sb.append("    nextStep: ").append(toIndentedString(nextStep)).append("\n");
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
