package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

public class UserAuthenticationRequest {

    private String username;
    private String password;
    private String clientId;

    /**
     * username of the user
     **/
    public UserAuthenticationRequest username(String username) {

        this.username = username;
        return this;
    }

    @ApiModelProperty(value = "username of the user")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * password of the user
     **/
    public UserAuthenticationRequest password(String password) {

        this.password = password;
        return this;
    }

    @ApiModelProperty(value = "password of the user")
    @JsonProperty("password")
    @Valid
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * client Id of the service provider
     **/
    public UserAuthenticationRequest clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }

    @ApiModelProperty(value = "client Id of the service provider")
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
        UserAuthenticationRequest userAuthenticationRequest = (UserAuthenticationRequest) o;
        return Objects.equals(this.username, userAuthenticationRequest.username) &&
                Objects.equals(this.password, userAuthenticationRequest.password) &&
                Objects.equals(this.clientId, userAuthenticationRequest.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, clientId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserAuthenticationRequest {\n");

        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
