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
public class AuthenticationValidationFailureReason {

    private String code;
    private String message;
    private String description;

    /**
     **/
    public AuthenticationValidationFailureReason code(String code) {

        this.code = code;
        return this;
    }

    @ApiModelProperty(example = "SMS-60006", value = "")
    @JsonProperty("code")
    @Valid
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * OTP validation failure reason.
     **/
    public AuthenticationValidationFailureReason message(String message) {

        this.message = message;
        return this;
    }

    @ApiModelProperty(example = "Expired OTP.", value = "OTP validation failure reason.")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * OTP validation failure reason description.
     **/
    public AuthenticationValidationFailureReason description(String description) {

        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "Expired OTP provided for the user Id: 8b1cc9c4-b671-448a-a334-5afe838a4a3b.",
            value = "OTP validation failure reason description.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationValidationFailureReason authValidationFailureReason = (AuthenticationValidationFailureReason) o;
        return Objects.equals(this.code, authValidationFailureReason.code) &&
                Objects.equals(this.message, authValidationFailureReason.message) &&
                Objects.equals(this.description, authValidationFailureReason.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationValidationFailureReason {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
