package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.AuthenticationStepDetailsDTO;
import java.util.ArrayList;
import java.util.Objects;
import javax.validation.Valid;

public class AuthenticationStepsResponse {

    private ArrayList<AuthenticationStepDetailsDTO> authenticationStepDetails;

    private Object authenticationSteps;

    public AuthenticationStepsResponse authenticationSteps(Object authenticationSteps) {

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

    public AuthenticationStepsResponse setAutheticatorStepDetails
            (ArrayList<AuthenticationStepDetailsDTO> authenticationStepDetails) {

        this.authenticationStepDetails = authenticationStepDetails;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationStepsResponse userAuthenticationResponse = (AuthenticationStepsResponse) o;
        return Objects.equals(this.authenticationSteps, userAuthenticationResponse.authenticationSteps);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authenticationSteps);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationStepsResponse {\n");
        sb.append("    authenticationSteps: ").append(toIndentedString(authenticationSteps)).append("\n");
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
