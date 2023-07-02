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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Authentication Flow Id object model.
 */
public class FlowIdDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String fullQualifiedUserName;
    private String flowIdIdentifier;
    private String flowId;
    private String flowIdState;
    private long generatedTime;
    private long expiryTime;
    private boolean isAuthFlowCompleted;
    private int serviceProviderAppId;
    private int spTenantId;
    private int userTenantId;
    private LinkedHashMap<Integer, String> authenticatedSteps;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullQualifiedUserName() {
        return fullQualifiedUserName;
    }

    public void setFullQualifiedUserName(String fullQualifiedUserName) {
        this.fullQualifiedUserName = fullQualifiedUserName;
    }

    public String getFlowIdIdentifier() {
        return flowIdIdentifier;
    }

    public void setFlowIdIdentifier(String flowIdIdentifier) {
        this.flowIdIdentifier = flowIdIdentifier;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowIdState() {
        return flowIdState;
    }

    public void setFlowIdState(String flowIdState) {
        this.flowIdState = flowIdState;
    }

    public long getGeneratedTime() {

        return generatedTime;
    }

    public void setGeneratedTime(long generatedTime) {

        this.generatedTime = generatedTime;
    }

    public long getExpiryTime() {

        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {

        this.expiryTime = expiryTime;
    }

    public boolean isAuthFlowCompleted () {

        return isAuthFlowCompleted;
    }

    public void setAuthFlowCompleted (boolean isAuthFlowCompleted) {

        this.isAuthFlowCompleted = isAuthFlowCompleted;
    }

    public int getServiceProviderAppId() {

        return serviceProviderAppId;
    }

    public void setServiceProviderAppId(int serviceProviderAppId) {

        this.serviceProviderAppId = serviceProviderAppId;
    }

    public LinkedHashMap<Integer, String> getAuthenticatedSteps() {

        return authenticatedSteps;
    }

    public int getSpTenantId() {

        return spTenantId;
    }

    public void setSpTenantId(int spTenantId) {

        this.spTenantId = spTenantId;
    }

    public int getUserTenantId() {

        return userTenantId;
    }

    public void setUserTenantId(int userTenantId) {

        this.userTenantId = userTenantId;
    }

    public void setAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {

        this.authenticatedSteps = authenticatedSteps;
    }
}
