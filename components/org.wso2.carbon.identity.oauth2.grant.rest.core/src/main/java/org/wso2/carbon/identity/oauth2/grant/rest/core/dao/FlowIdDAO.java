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

package org.wso2.carbon.identity.oauth2.grant.rest.core.dao;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationServerException;

/**
 * CacheBackedFlowIDDAO and FlowIdDAOImpl class is implemented via this interface.
 */
public interface FlowIdDAO {
    /**
     * This method is to insert FlowId Data.
     *
     * @param flowIdDO 	Flow Id Data Object.
     * @throws AuthenticationException if failed to insert Flow Id data.
     */
    void addFlowIdData(FlowIdDO flowIdDO) throws AuthenticationException;

    /**
     * This method is to retrieve FlowId Data.
     *
     * @param flowId		           UUID to track the flow.
     * @throws AuthenticationException If failed to retrieve FlowId data.
     * @return FlowIdDO                Returns a flowId object.
     */
    FlowIdDO getFlowIdData(String flowId) throws AuthenticationException;

    /**
     * This method is to renew the FlowId.
     *
     * @param previousFlowIdIdentifier Previous FlowId Identifier.
     * @param prevFlowId 		       Previous FlowId.
     * @param flowIdDO 		           FlowId data object with a new FlowId and a new FlowId Identifier.
     * @throws AuthenticationException if failed to renew the FlowId.
     */
    void refreshFlowId(String previousFlowIdIdentifier, String prevFlowId, FlowIdDO flowIdDO)
            throws AuthenticationException;

    /**
     * This method is to update FlowId State.
     *
     * @param flowIdIdentifier 	       UUID to uniquely identify the FlowId and related data.
     * @param flowIdState 	           FlowId state
     * @throws AuthenticationException if failed to update FlowId State.
     */
    void updateFlowIdState(String flowIdIdentifier, String flowIdState) throws AuthenticationException;

    /**
     * This method is to add a successfully authenticated step to the DB.
     *
     * @param stepNo 	                Authentication step number.
     * @param authenticator 	        Authenticator name
     * @param flowIdIdentifier 	        UUID to uniquely identify the FlowId and related data
     * @throws AuthenticationException  if failed to add authenticated step.
     */
    void addAuthenticatedStep (int stepNo, String authenticator, String flowIdIdentifier)
            throws AuthenticationException;

    /**
     * This method will return the FlowId Identifier for the given FlowId if it exists.
     *
     * @param flowId                         FlowId to get the FlowId Identifier.
     * @return                               FlowId Identifier.
     * @throws AuthenticationServerException If failed to get the FlowId Identifier.
     */
    default String getFlowIdIdentifier(String flowId) throws AuthenticationServerException {

        return null;
    }

    /**
     * This method will update the data of an existing flow id and set it as the current active flow id for the user.
     *
     * @param prevFlowIdIdentifier 	        FlowId currently set as active for the user.
     * @param existingFlowIdentifier 	    Existing FlowId to be updated.
     * @param flowIdDO 		                FlowId data object with the updated data.
     * @throws AuthenticationException      if failed to update the existing FlowId.
     */
    default void updateExistingFlowId(String prevFlowIdIdentifier, String existingFlowIdentifier, FlowIdDO flowIdDO)
            throws AuthenticationException {

    }

}
