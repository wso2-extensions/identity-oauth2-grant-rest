/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthenticationValidationRequest  {
  
    private String authenticator;
    private String flowId;
    private String clientId;
    private String userIdentifier;
    private String password;

    /**
    * Selected Authenticator to proceed with the authentication flow.
    **/
    public AuthenticationValidationRequest authenticator(String authenticator) {

        this.authenticator = authenticator;
        return this;
    }
    
    @ApiModelProperty(example = "EmailOTP", required = true, value = "Selected Authenticator to proceed with the authentication flow.")
    @JsonProperty("authenticator")
    @Valid
    @NotNull(message = "Property authenticator cannot be null.")

    public String getAuthenticator() {
        return authenticator;
    }
    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    /**
    * A unique identifier that can be used to uniquely identify the transaction. This is &lt;b&gt;mandatory&lt;/b&gt; if this is not the first step of the authentication flow.
    **/
    public AuthenticationValidationRequest flowId(String flowId) {

        this.flowId = flowId;
        return this;
    }
    
    @ApiModelProperty(example = "1cb0c52f-c19b-493f-a535-94b2977d1ad5", value = "A unique identifier that can be used to uniquely identify the transaction. This is <b>mandatory</b> if this is not the first step of the authentication flow.")
    @JsonProperty("flowId")
    @Valid
    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
    * The Client Id of the Oauth2/OpenId configuration of the Service Provider. This is &lt;b&gt;mandatory&lt;/b&gt; if this is the first step of the authentication flow.
    **/
    public AuthenticationValidationRequest clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }
    
    @ApiModelProperty(example = "eENJCNjdnbJSS!WJNDI892", value = "The Client Id of the Oauth2/OpenId configuration of the Service Provider. This is <b>mandatory</b> if this is the first step of the authentication flow.")
    @JsonProperty("clientId")
    @Valid
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
    * A unique identifier that can be used to uniquely identify the user. This is &lt;b&gt;mandatory&lt;/b&gt; if this is the first step of the authentication flow.
    **/
    public AuthenticationValidationRequest userIdentifier(String userIdentifier) {

        this.userIdentifier = userIdentifier;
        return this;
    }
    
    @ApiModelProperty(example = "1cb0c52f-c19b-493f-a535-94b2977d1ad5", value = "A unique identifier that can be used to uniquely identify the user. This is <b>mandatory</b> if this is the first step of the authentication flow.")
    @JsonProperty("userIdentifier")
    @Valid
    public String getUserIdentifier() {
        return userIdentifier;
    }
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    /**
    * This is the secret to be verified against the user. This is &lt;b&gt;mandatory&lt;/b&gt; if this is the first step of the authentication flow or if the selected authenticator is BasicAuthenticator.
    **/
    public AuthenticationValidationRequest password(String password) {

        this.password = password;
        return this;
    }
    
    @ApiModelProperty(example = "123456789", value = "This is the secret to be verified against the user. This is <b>mandatory</b> if this is the first step of the authentication flow or if the selected authenticator is BasicAuthenticator.")
    @JsonProperty("password")
    @Valid
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationValidationRequest authenticationValidationRequest = (AuthenticationValidationRequest) o;
        return Objects.equals(this.authenticator, authenticationValidationRequest.authenticator) &&
            Objects.equals(this.flowId, authenticationValidationRequest.flowId) &&
            Objects.equals(this.clientId, authenticationValidationRequest.clientId) &&
            Objects.equals(this.userIdentifier, authenticationValidationRequest.userIdentifier) &&
            Objects.equals(this.password, authenticationValidationRequest.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticator, flowId, clientId, userIdentifier, password);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationValidationRequest {\n");
        
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
        sb.append("    flowId: ").append(toIndentedString(flowId)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    userIdentifier: ").append(toIndentedString(userIdentifier)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

