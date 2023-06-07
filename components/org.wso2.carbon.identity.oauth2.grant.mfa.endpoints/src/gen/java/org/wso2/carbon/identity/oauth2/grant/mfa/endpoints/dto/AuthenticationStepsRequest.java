package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;


public class AuthenticationStepsRequest {

    private String clientId;

    /**
     * Client id of the OAuth2 Service provider.
     **/
    public AuthenticationStepsRequest clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }

    @ApiModelProperty(value = "A client Id that can be used to uniquely identify the OAuth2 Service Provider")
    @JsonProperty("clientId")
    @Valid
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationStepsRequest authenticationStepRequest = (AuthenticationStepsRequest) o;
        return Objects.equals(this.clientId, authenticationStepRequest.clientId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationStepsRequest {\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
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
