package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
public class MFAValidationFailureReason {

    private String code;
    private String message;
    private String description;

    /**
     **/
    public MFAValidationFailureReason code(String code) {

        this.code = code;
        return this;
    }

    @ApiModelProperty(example = "SMS-60006", value = "")
    @JsonProperty("code")
    @Valid
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * OTP validation failure reason.
     **/
    public MFAValidationFailureReason message(String message) {

        this.message = message;
        return this;
    }

    @ApiModelProperty(example = "Expired OTP.", value = "OTP validation failure reason.")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * OTP validation failure reason description.
     **/
    public MFAValidationFailureReason description(String description) {

        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "Expired OTP provided for the user Id: 8b1cc9c4-b671-448a-a334-5afe838a4a3b.",
            value = "OTP validation failure reason description.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MFAValidationFailureReason mfaValidationFailureReason = (MFAValidationFailureReason) o;
        return Objects.equals(this.code, mfaValidationFailureReason.code) &&
                Objects.equals(this.message, mfaValidationFailureReason.message) &&
                Objects.equals(this.description, mfaValidationFailureReason.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MfaValidationFailureReason {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
