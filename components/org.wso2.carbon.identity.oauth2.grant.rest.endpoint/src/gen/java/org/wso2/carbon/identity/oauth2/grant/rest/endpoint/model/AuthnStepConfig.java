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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.model.AuthenticatorConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthnStepConfig  {
  
    private Integer stepNo;
    private List<AuthenticatorConfig> authenticatorConfigs = null;


    /**
    **/
    public AuthnStepConfig stepNo(Integer stepNo) {

        this.stepNo = stepNo;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "")
    @JsonProperty("stepNo")
    @Valid
    public Integer getStepNo() {
        return stepNo;
    }
    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    /**
    **/
    public AuthnStepConfig authenticatorConfigs(List<AuthenticatorConfig> authenticatorConfigs) {

        this.authenticatorConfigs = authenticatorConfigs;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authenticatorConfigs")
    @Valid
    public List<AuthenticatorConfig> getAuthenticatorConfigs() {
        return authenticatorConfigs;
    }
    public void setAuthenticatorConfigs(List<AuthenticatorConfig> authenticatorConfigs) {
        this.authenticatorConfigs = authenticatorConfigs;
    }

    public AuthnStepConfig addAuthenticatorConfigsItem(AuthenticatorConfig authenticatorConfigsItem) {
        if (this.authenticatorConfigs == null) {
            this.authenticatorConfigs = new ArrayList<>();
        }
        this.authenticatorConfigs.add(authenticatorConfigsItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthnStepConfig authnStepConfig = (AuthnStepConfig) o;
        return Objects.equals(this.stepNo, authnStepConfig.stepNo) &&
            Objects.equals(this.authenticatorConfigs, authnStepConfig.authenticatorConfigs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepNo, authenticatorConfigs);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthnStepConfig {\n");
        
        sb.append("    stepNo: ").append(toIndentedString(stepNo)).append("\n");
        sb.append("    authenticatorConfigs: ").append(toIndentedString(authenticatorConfigs)).append("\n");
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

