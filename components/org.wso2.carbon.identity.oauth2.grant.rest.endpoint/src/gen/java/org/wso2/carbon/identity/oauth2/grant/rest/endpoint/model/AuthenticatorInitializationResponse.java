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

public class AuthenticatorInitializationResponse  {
  
    private String flowId;
    private String authenticator;

    /**
    * A refreshed token that can be used to uniquely identify the transaction
    **/
    public AuthenticatorInitializationResponse flowId(String flowId) {

        this.flowId = flowId;
        return this;
    }
    
    @ApiModelProperty(example = "1cb0c52f-c19b-493f-a535-94b2977d1ad5", required = true, value = "A refreshed token that can be used to uniquely identify the transaction")
    @JsonProperty("flowId")
    @Valid
    @NotNull(message = "Property flowId cannot be null.")

    public String getFlowId() {
        return flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
    * Authenticator
    **/
    public AuthenticatorInitializationResponse authenticator(String authenticator) {

        this.authenticator = authenticator;
        return this;
    }
    
    @ApiModelProperty(example = "SMSOTP", required = true, value = "Authenticator")
    @JsonProperty("authenticator")
    @Valid
    @NotNull(message = "Property authenticator cannot be null.")

    public String getAuthenticator() {
        return authenticator;
    }
    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticatorInitializationResponse authenticatorInitializationResponse = (AuthenticatorInitializationResponse) o;
        return Objects.equals(this.flowId, authenticatorInitializationResponse.flowId) &&
            Objects.equals(this.authenticator, authenticatorInitializationResponse.authenticator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowId, authenticator);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticatorInitializationResponse {\n");
        
        sb.append("    flowId: ").append(toIndentedString(flowId)).append("\n");
        sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
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

