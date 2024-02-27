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

package org.wso2.carbon.identity.oauth2.grant.rest.core.dao;

/**
 * Flow Id related sql queries.
 */
public class SQLQueries {

    public static final String ADD_FLOW_ID = "INSERT INTO IDN_AUTH_REST_FLOW (FLOW_ID_IDENTIFIER, " +
            "FLOW_ID, FLOW_ID_STATE, TIME_GENERATED, EXPIRY_TIME, IS_AUTH_FLOW_COMPLETED, SP_APP_ID, SP_TENANT_ID, "
            + "LOGGED_USER_CLAIM) VALUES (?,?,?,?,?,?,?,?,?);";
    public static final String ADD_FLOW_ID_USER_DATA = "INSERT INTO IDN_REST_AUTH_USER " +
            "(USER_TENANT_ID, USER_ID, USERNAME, FLOW_ID_IDENTIFIER) VALUES (?,?,?,?);";
    public static final String UPDATE_FLOW_ID_STATE = "UPDATE IDN_AUTH_REST_FLOW SET FLOW_ID_STATE=? " +
            "WHERE FLOW_ID=? ;";
    public static final String RETRIEVE_FLOW_ID_DATA =
            "SELECT MT.FLOW_ID_IDENTIFIER, MT.FLOW_ID, MT.FLOW_ID_STATE,MT.TIME_GENERATED, MT.EXPIRY_TIME, \n" +
                    "MT.IS_AUTH_FLOW_COMPLETED, MT.SP_APP_ID, MT.SP_TENANT_ID, MU.USER_TENANT_ID, MU.USER_ID, \n" +
                    "MU.USERNAME, MT.LOGGED_USER_CLAIM FROM IDN_AUTH_REST_FLOW MT,IDN_REST_AUTH_USER MU\n" +
                    "WHERE MU.FLOW_ID_IDENTIFIER = MT.FLOW_ID_IDENTIFIER AND MT.FLOW_ID = ?;";
    public static final String ADD_AUTHENTICATED_STEP = "INSERT INTO IDN_REST_AUTH_AUTHENTICATED_STEPS " +
            "(DATA_KEY, DATA_VALUE, FLOW_ID_IDENTIFIER) VALUES (?,?,?);";
    public static final String GET_AUTHENTICATED_STEPS = "SELECT DATA_KEY, DATA_VALUE FROM " +
            "IDN_REST_AUTH_AUTHENTICATED_STEPS WHERE FLOW_ID_IDENTIFIER = ?";
    public static final String REFRESH_FLOW_ID_IDENTIFIER_IDN_AUTH_USER = "UPDATE IDN_REST_AUTH_USER SET " +
            "FLOW_ID_IDENTIFIER = ? WHERE FLOW_ID_IDENTIFIER = ?";
    public static final String REFRESH_FLOW_ID_IDENTIFIER_IDN_AUTH_STEPS = "UPDATE IDN_REST_AUTH_AUTHENTICATED_STEPS " +
            "SET FLOW_ID_IDENTIFIER = ? WHERE FLOW_ID_IDENTIFIER = ?";
}
