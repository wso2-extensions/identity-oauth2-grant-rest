package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserAuthenticationError {

    private String code;
    private String message;
    private String description;
    private String traceId;

    /**
     **/
    public UserAuthenticationError code(String code) {

        this.code = code;
        return this;
    }

    @ApiModelProperty(example = "60001", required = true, value = "")
    @JsonProperty("code")
    @Valid
    @NotNull(message = "Property code cannot be null.")

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
     **/
    public UserAuthenticationError message(String message) {

        this.message = message;
        return this;
    }

    @ApiModelProperty(example = "Some error message", required = true, value = "")
    @JsonProperty("message")
    @Valid
    @NotNull(message = "Property message cannot be null.")

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     **/
    public UserAuthenticationError description(String description) {

        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "Some error description", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     **/
    public UserAuthenticationError traceId(String traceId) {

        this.traceId = traceId;
        return this;
    }

    @ApiModelProperty(example = "a0fbcfeb-3617-43c4-8dd0-7b7d38e13047", value = "")
    @JsonProperty("traceId")
    @Valid
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAuthenticationError userAuthenticationError = (UserAuthenticationError) o;
        return Objects.equals(this.code, userAuthenticationError.code) &&
                Objects.equals(this.message, userAuthenticationError.message) &&
                Objects.equals(this.description, userAuthenticationError.description) &&
                Objects.equals(this.traceId, userAuthenticationError.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, description, traceId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserAuthenticationError {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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
