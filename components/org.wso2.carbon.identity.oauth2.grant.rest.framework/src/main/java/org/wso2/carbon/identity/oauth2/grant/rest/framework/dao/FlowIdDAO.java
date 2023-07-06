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
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;

/**
 * CacheBackedFlowIDDAO and FlowIdDAOImpl class is implemented via this interface.
 */
public interface FlowIdDAO {
    /**
     * This method is to insert Flow Id Data.
     *
     * @param flowIdDO 	Flow Id Data Object.
     * @throws AuthenticationException if failed to insert Flow Id data.
     */
    void addFlowIdData(FlowIdDO flowIdDO) throws AuthenticationException;

    /**
     * This method is to retrieve Flow Id Data.
     *
     * @param flowId		    UUID to track the flow.
     * @throws AuthenticationException If failed to retrieve Flow Id data.
     */
    FlowIdDO getFlowIdData(String flowId) throws AuthenticationException;

    /**
     * This method is to renew the Flow Id.
     *
     * @param prevFlowId 		Previous Flow Id.
     * @param flowIdDO 		    Flow Id data object with a new Flow Id and a new Flow Id Identifier.
     * @throws AuthenticationException if failed to renew the Flow Id.
     */
    void refreshFlowId(String previousFlowIdIdentifier, String prevFlowId, FlowIdDO flowIdDO)
            throws AuthenticationException;

    /**
     * This method is to update Flow Id State.
     *
     * @param flowIdIdentifier 	UUID to uniquely identify the Flow Id and related data.
     * @param flowIdState 	    Flow Id state
     * @throws AuthenticationException if failed to update Flow Id State.
     */
    void updateFlowIdState(String flowIdIdentifier, String flowIdState) throws AuthenticationException;

    /**
     * This method is to add a successfully authenticated step to the DB.
     *
     * @param stepNo 	        Authentication step number.
     * @param authenticator 	Authenticator name
     * @param flowIdIdentifier 	UUID to uniquely identify the Flow Id and related data
     * @throws AuthenticationException if failed to add authenticated step.
     */
    void addAuthenticatedStep (int stepNo, String authenticator, String flowIdIdentifier)
            throws AuthenticationException;

}
