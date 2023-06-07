package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

public class MFAInitializationResponse {

    private String mfaToken;
    private String authenticator;
    private String otp;

    /**
     * A refreshed token that can be used to uniquely identify the transaction
     **/
    public MFAInitializationResponse mfaToken(String mfaToken) {

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
    public MFAInitializationResponse authenticator(String authenticator) {

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
    public MFAInitializationResponse otp(String otp) {

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
        MFAInitializationResponse mfaInitializationResponse = (MFAInitializationResponse) o;
        return Objects.equals(this.mfaToken, mfaInitializationResponse.mfaToken) &&
                Objects.equals(this.authenticator, mfaInitializationResponse.authenticator) &&
                Objects.equals(this.otp, mfaInitializationResponse.otp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mfaToken, authenticator, otp);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MfaInitializationResponse {\n");

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
