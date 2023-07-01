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

public class AuthenticatorInitializationRequest  {
  
    private String authenticator;
    private String flowId;

    /**
    * Selected Authenticator from the authenticator list.
    **/
    public AuthenticatorInitializationRequest authenticator(String authenticator) {

        this.authenticator = authenticator;
        return this;
    }
    
    @ApiModelProperty(example = "SMSOTP", required = true, value = "Selected Authenticator from the authenticator list.")
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
    * A refreshed identitfier that can be used to uniquely identify the authentication transaction
    **/
    public AuthenticatorInitializationRequest flowId(String flowId) {

        this.flowId = flowId;
        return this;
    }
    
    @ApiModelProperty(example = "3da97d86-91ca-4ae7-b225-5e75f6228d15", required = true, value = "A refreshed identitfier that can be used to uniquely identify the authentication transaction")
    @JsonProperty("flowId")
    @Valid
    @NotNull(message = "Property flowId cannot be null.")

    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticatorInitializationRequest authenticatorInitializationRequest = (AuthenticatorInitializationRequest) o;
        return Objects.equals(this.authenticator, authenticatorInitializationRequest.authenticator) &&
            Objects.equals(this.flowId, authenticatorInitializationRequest.flowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticator, flowId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticatorInitializationRequest {\n");
        
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
        sb.append("    flowId: ").append(toIndentedString(flowId)).append("\n");
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

