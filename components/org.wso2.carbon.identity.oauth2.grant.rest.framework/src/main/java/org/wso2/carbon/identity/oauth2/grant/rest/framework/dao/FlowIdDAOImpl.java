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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.exception.AuthenticationServerException;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.util.RestAuthUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * This class is used to implement the functions for DB layer.
 */
public class FlowIdDAOImpl implements FlowIdDAO {

    private static final Log LOG = LogFactory.getLog(FlowIdDAOImpl.class);
    private static volatile FlowIdDAOImpl instance;
    public static FlowIdDAOImpl getInstance() {
        if (instance == null) {
            synchronized (FlowIdDAOImpl.class) {
                if (instance == null) {
                    instance = new FlowIdDAOImpl();
                }
            }
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlowIdData(FlowIdDO flowIdDO) throws AuthenticationException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding Flow ID data to the database.");
            }

            try (PreparedStatement prepStmt1 = connection.prepareStatement(SQLQueries.ADD_FLOW_ID);
                 PreparedStatement prepStmt2 = connection.prepareStatement(SQLQueries.ADD_FLOW_ID_USER_DATA)) {

                prepStmt1.setString(1, flowIdDO.getFlowIdIdentifier());
                prepStmt1.setString(2, flowIdDO.getFlowId());
                prepStmt1.setString(3, flowIdDO.getFlowIdState());
                prepStmt1.setLong(4, flowIdDO.getGeneratedTime());
                prepStmt1.setLong(5, flowIdDO.getExpiryTime());
                prepStmt1.setString(6, String.valueOf(flowIdDO.isAuthFlowCompleted()));
                prepStmt1.setInt(7, flowIdDO.getServiceProviderAppId());
                prepStmt1.setInt(8, flowIdDO.getSpTenantId());

                prepStmt1.execute();

                prepStmt2.setInt(1, flowIdDO.getUserTenantId());
                prepStmt2.setString(2, flowIdDO.getUserId());
                prepStmt2.setString(3, flowIdDO.getFullQualifiedUserName());
                prepStmt2.setString(4, flowIdDO.getFlowIdIdentifier());
                prepStmt2.execute();

                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new AuthenticationServerException("Error while inserting flow Id data to the database.", e);
            }

        } catch (SQLException e) {
            throw new AuthenticationServerException("Error while closing connection after inserting Flow Id data to " +
                    "the database.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowIdDO getFlowIdData(String flowId) throws AuthenticationException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.RETRIEVE_FLOW_ID_DATA)) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Get Flow ID data from the database.");
            }

            prepStmt.setString(1, flowId);

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {

                    FlowIdDO flowIdDO = new FlowIdDO();
                    flowIdDO.setUserId(resultSet.getString(Constants.DB_FIELD_USER_ID));
                    flowIdDO.setFullQualifiedUserName(resultSet.getString(Constants.DB_FIELD_USERNAME));
                    flowIdDO.setUserTenantId(resultSet.getInt(Constants.DB_FIELD_USER_TENANT_ID));
                    flowIdDO.setFlowIdIdentifier(resultSet.getString(Constants.DB_FIELD_FLOW_ID_IDENTIFIER));
                    flowIdDO.setFlowId(resultSet.getString(Constants.DB_FIELD_FLOW_ID));
                    flowIdDO.setSpTenantId(resultSet.getInt(Constants.DB_FIELD_SP_TENANT_ID));
                    flowIdDO.setFlowIdState(resultSet.getString(Constants.DB_FIELD_FLOW_ID_STATE));
                    flowIdDO.setGeneratedTime(resultSet.getLong(Constants.DB_FIELD_TIME_GENERATED));
                    flowIdDO.setExpiryTime(resultSet.getLong(Constants.DB_FIELD_EXPIRY_TIME));
                    flowIdDO.setAuthFlowCompleted(resultSet.getBoolean(Constants.DB_FIELD_IS_AUTH_FLOW_COMPLETED));
                    flowIdDO.setServiceProviderAppId(resultSet.getInt(Constants.DB_FIELD_SP_APP_ID));
                    flowIdDO.setAuthenticatedSteps(getAuthenticatedSteps
                            (connection, flowIdDO.getFlowIdIdentifier()));

                    return flowIdDO;
                }
            }
        } catch (SQLException e) {
            throw new AuthenticationServerException("Error while retrieving flowId data.", e);
        }

        throw RestAuthUtil.handleClientException(
                Constants.ErrorMessage.CLIENT_INVALID_FLOW_ID, flowId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshFlowId(String previousFlowIdIdentifier, String prevFlowId, FlowIdDO flowIdDO)
            throws AuthenticationException {

        updateFlowIdState(prevFlowId, Constants.FLOW_ID_STATE_INACTIVE);
        addRefreshedFlowIdData(previousFlowIdIdentifier, flowIdDO);
    }

    @Override
    public void updateFlowIdState(String flowIdIdentifier, String flowIdState) throws AuthenticationException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Changing the Flow ID state as INACTIVE for used Flow IDs.");
            }

            try (PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.UPDATE_FLOW_ID_STATE)) {

                prepStmt.setString(1, flowIdState);
                prepStmt.setString(2, flowIdIdentifier);

                prepStmt.execute();
                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new AuthenticationServerException
                        ("Error while updating the Flow ID state from the database.", e);
            }

        } catch (SQLException e) {
            throw new AuthenticationServerException
                    ("Error while closing connection after changing the state of used Flow IDs.", e);
        }
    }

    @Override
    public void addAuthenticatedStep(int stepNo, String authenticator, String flowIdIdentifier)
            throws AuthenticationException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding Flow ID data to the Authentication Steps table.");
            }
            try (PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.ADD_AUTHENTICATED_STEP)) {

                prepStmt.setInt(1, stepNo);
                prepStmt.setString(2, authenticator);
                prepStmt.setString(3, flowIdIdentifier);

                prepStmt.execute();
                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new AuthenticationServerException("Error while inserting Flow ID data to the database.", e);
            }

        } catch (SQLException e) {
            throw new AuthenticationServerException("Error while closing connection after inserting Flow ID data to " +
                    "the database.", e);
        }
    }

    public void addRefreshedFlowIdData(String prevFlowIdIdentifier, FlowIdDO flowIdDO) throws AuthenticationException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding refreshed Flow ID data to the database.");
            }

            try (PreparedStatement prepStmt1 = connection.prepareStatement(SQLQueries.ADD_FLOW_ID);
                 PreparedStatement prepStmt2 =
                         connection.prepareStatement(SQLQueries.REFRESH_FLOW_ID_IDENTIFIER)) {

                prepStmt1.setString(1, flowIdDO.getFlowIdIdentifier());
                prepStmt1.setString(2, flowIdDO.getFlowId());
                prepStmt1.setString(3, flowIdDO.getFlowIdState());
                prepStmt1.setLong(4, flowIdDO.getGeneratedTime());
                prepStmt1.setLong(5, flowIdDO.getExpiryTime());
                prepStmt1.setString(6, String.valueOf(flowIdDO.isAuthFlowCompleted()));
                prepStmt1.setInt(7, flowIdDO.getServiceProviderAppId());
                prepStmt1.setInt(8, flowIdDO.getSpTenantId());

                prepStmt1.execute();

                prepStmt2.setString(1, flowIdDO.getFlowIdIdentifier());
                prepStmt2.setString(2, flowIdDO.getFlowIdIdentifier());
                prepStmt2.setString(3, prevFlowIdIdentifier);

                prepStmt2.execute();
                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new AuthenticationServerException("Error while adding refreshed Flow Id data to the database.",
                        e);
            }

        } catch (SQLException e) {
            throw new AuthenticationServerException
                    ("Error while closing connection after adding refreshed Flow Id data to the database.", e);
        }
    }

    public LinkedHashMap<Integer, String> getAuthenticatedSteps(Connection connection, String flowIdIdentifier)
            throws AuthenticationException {

        try (PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.GET_AUTHENTICATED_STEPS)) {
            prepStmt.setString(1, flowIdIdentifier);

            LinkedHashMap<Integer, String> authenticatedSteps = new LinkedHashMap<>();

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                while (resultSet.next()) {
                    authenticatedSteps.put(resultSet.getInt(Constants.DB_FIELD_DATA_KEY),
                            resultSet.getString(Constants.DB_FIELD_DATA_VALUE));
                }
                return authenticatedSteps;
            }
        } catch (SQLException e) {
            throw new AuthenticationServerException("Error while retrieving authenticated steps.", e);
        }
    }
}
