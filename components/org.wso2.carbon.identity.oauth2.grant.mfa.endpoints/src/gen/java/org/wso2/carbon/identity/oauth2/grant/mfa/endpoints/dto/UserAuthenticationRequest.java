/*
 *  Copyright (c) 2023, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC licenses this file to you under the Apache license,
 *  Version 2.0 (the "license"); you may not use this file except
 *  in compliance with the license.
 *  You may obtain a copy of the license at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;

public class UserAuthenticationRequest {
    private String authenticator;
    private String password;
    private String clientId;
    private String flowId;
    private String userIdentifier;

    /**
     * authenticator passed via JSON payload.
     **/
    public UserAuthenticationRequest authenticator(String authenticator) {
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
     * password of the user.
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
     * client Id of the service provider.
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

    /**
     * Flow Id passed via JSON payload.
     **/
    public UserAuthenticationRequest flowId(String flowId) {
        this.flowId = flowId;
        return this;
    }

    @ApiModelProperty(example = "35468bb9-5194-4bb9-b727-a670364bf2df", value = "flowId")
    @JsonProperty("flowId")
    @Valid
    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * username of the user.
     **/
    public UserAuthenticationRequest userIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
        return this;
    }

    @ApiModelProperty(value = "userIdentifier of the user")
    @JsonProperty("userIdentifier")
    @Valid
    public String getUserIdentifier() {
        return userIdentifier;
    }
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
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
        return Objects.equals(this.userIdentifier, userAuthenticationRequest.userIdentifier) &&
                Objects.equals(this.password, userAuthenticationRequest.password) &&
                Objects.equals(this.clientId, userAuthenticationRequest.clientId) &&
                Objects.equals(this.authenticator, userAuthenticationRequest.authenticator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdentifier, password, clientId, authenticator);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserAuthenticationRequest {\n");
        sb.append("    userIdentifier: ").append(toIndentedString(userIdentifier)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
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
