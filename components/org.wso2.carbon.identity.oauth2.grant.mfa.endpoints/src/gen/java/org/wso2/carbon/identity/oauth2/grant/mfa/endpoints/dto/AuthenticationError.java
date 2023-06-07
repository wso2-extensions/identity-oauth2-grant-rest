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
import javax.validation.constraints.NotNull;
public class AuthenticationError {
    private String authenticator;
    private String code;
    private String message;
    private String description;
    private String traceId;

    /**
     * Authenticator.
     **/
    public AuthenticationError authenticator(String authenticator) {

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

    /**
     **/
    public AuthenticationError code(String code) {

        this.code = code;
        return this;
    }

    @ApiModelProperty(example = "SMS-60001", required = true, value = "")
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
    public AuthenticationError message(String message) {

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
    public AuthenticationError description(String description) {

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
    public AuthenticationError traceId(String traceId) {

        this.traceId = traceId;
        return this;
    }

    @ApiModelProperty(example = "e0fbcfeb-3617-43c4-8dd0-7b7d38e13047", value = "")
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
        AuthenticationError authnError = (AuthenticationError) o;
        return Objects.equals(this.authenticator, authnError.authenticator) &&
                Objects.equals(this.code, authnError.code) &&
                Objects.equals(this.message, authnError.message) &&
                Objects.equals(this.description, authnError.description) &&
                Objects.equals(this.traceId, authnError.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticator, code, message, description, traceId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthError {\n");

        sb.append("authenticator: ").append(toIndentedString(authenticator)).append("\n");
        sb.append("code: ").append(toIndentedString(code)).append("\n");
        sb.append("message: ").append(toIndentedString(message)).append("\n");
        sb.append("description: ").append(toIndentedString(description)).append("\n");
        sb.append("traceId: ").append(toIndentedString(traceId)).append("\n");
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
