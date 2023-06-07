package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

public class MFAValidationRequest {

    private String userId;
    private String mfaToken;
    private String authenticator;
    private String otp;

    /**
     * SCIM ID of the user
     **/
    public MFAValidationRequest userId(String userId) {

        this.userId = userId;
        return this;
    }

    @ApiModelProperty(example = "8b1cc9c4-b671-448a-a334-5afe838a4a3b", value = "SCIM ID of the user")
    @JsonProperty("userId")
    @Valid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * A refreshed token that can be used to uniquely identify the transaction
     **/
    public MFAValidationRequest mfaToken(String mfaToken) {

        this.mfaToken = mfaToken;
        return this;
    }

    @ApiModelProperty(value = "A refreshed token that can be used to uniquely identify the transaction")
    @JsonProperty("mfa_token")
    @Valid
    public String getMfaToken() {
        return mfaToken;
    }
    public void setMfaToken(String mfaToken) {
        this.mfaToken = mfaToken;
    }

    /**
     * Authenticator
     **/
    public MFAValidationRequest authenticator(String authenticator) {

        this.authenticator = authenticator;
        return this;
    }

    @ApiModelProperty(example = "SMSOTP", value = "Authenticator")
    @JsonProperty("authenticator")
    @Valid
    public String getAuthenticator() {
        return authenticator;
    }
    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * The generated OTP
     **/
    public MFAValidationRequest otp(String otp) {

        this.otp = otp;
        return this;
    }

    @ApiModelProperty(value = "The generated OTP")
    @JsonProperty("otp")
    @Valid
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MFAValidationRequest mfaValidationRequest = (MFAValidationRequest) o;
        return Objects.equals(this.userId, mfaValidationRequest.userId) &&
                Objects.equals(this.mfaToken, mfaValidationRequest.mfaToken) &&
                Objects.equals(this.authenticator, mfaValidationRequest.authenticator) &&
                Objects.equals(this.otp, mfaValidationRequest.otp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, mfaToken, authenticator, otp);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MfaValidationRequest {\n");

        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    mfaToken: ").append(toIndentedString(mfaToken)).append("\n");
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
        sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
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
