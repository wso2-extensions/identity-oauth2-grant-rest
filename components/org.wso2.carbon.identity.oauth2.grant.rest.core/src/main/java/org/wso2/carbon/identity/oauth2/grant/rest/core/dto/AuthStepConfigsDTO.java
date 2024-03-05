/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.oauth2.grant.rest.core.dto;

import java.util.ArrayList;

/**
 * This class is used to keep single authentication step with one or multiple authenticator names.
 */
public class AuthStepConfigsDTO {
    private int stepNo;
    private ArrayList<AuthenticatorConfigDTO> authenticatorDetails;

    public void setStepNo(int stepNo) {

        this.stepNo = stepNo;
    }
    public void setAuthenticatorDetails(ArrayList<AuthenticatorConfigDTO> authenticatorDetails) {

        this.authenticatorDetails = authenticatorDetails;
    }
    public int getStepNo() {

        return stepNo;
    }
    public ArrayList<AuthenticatorConfigDTO> getAuthenticatorDetails() {

        return this.authenticatorDetails;
    }

}
