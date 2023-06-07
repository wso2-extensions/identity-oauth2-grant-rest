package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
public class MFAInitializationRequest {
    private String userId;
    private String authenticator;
    private String mfaToken;

    /**
     * SCIM Id of the user
     **/
    public MFAInitializationRequest userId(String userId) {

        this.userId = userId;
        return this;
    }

    @ApiModelProperty(example = "8b1cc9c4-b671-448a-a334-5afe838a4a3b", value = "SCIM Id of the user")
    @JsonProperty("userId")
    @Valid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Authenticator
     **/
    public MFAInitializationRequest authenticator(String authenticator) {

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
     * A refreshed token that can be used to uniquely identify the transaction
     **/
    public MFAInitializationRequest mfaToken(String mfaToken) {

        this.mfaToken = mfaToken;
        return this;
    }

    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ", value = "A refreshed token that can be used to uniquely identify the transaction")
    @JsonProperty("mfa_token")
    @Valid
    public String getMfaToken() {
        return mfaToken;
    }
    public void setMfaToken(String mfaToken) {
        this.mfaToken = mfaToken;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MFAInitializationRequest mfaInitializationRequest = (MFAInitializationRequest) o;
        return Objects.equals(this.userId, mfaInitializationRequest.userId) &&
                Objects.equals(this.authenticator, mfaInitializationRequest.authenticator) &&
                Objects.equals(this.mfaToken, mfaInitializationRequest.mfaToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, authenticator, mfaToken);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MfaInitializationRequest {\n");

        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
        sb.append("    mfaToken: ").append(toIndentedString(mfaToken)).append("\n");
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
