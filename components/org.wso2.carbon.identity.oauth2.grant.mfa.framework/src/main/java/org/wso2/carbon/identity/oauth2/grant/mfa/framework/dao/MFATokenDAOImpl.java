package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthServerException;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class MFATokenDAOImpl implements MFATokenDAO{

    private static final Log log = LogFactory.getLog(MFATokenDAOImpl.class);
    private static volatile MFATokenDAOImpl instance;

    public static MFATokenDAOImpl getInstance() {
        if (instance == null) {
            synchronized (MFATokenDAOImpl.class) {
                if (instance == null) {
                    instance = new MFATokenDAOImpl();
                }
            }
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMFATokenData(MFATokenDO mfaTokenDO) throws MFAAuthException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)){
            if (log.isDebugEnabled()) {
                log.debug("Adding MFA token data to the database.");
            }

            try (PreparedStatement prepStmt1 = connection.prepareStatement(SQLQueries.ADD_MFA_TOKEN_MYSQL);
                 PreparedStatement prepStmt2 = connection.prepareStatement(SQLQueries.ADD_MFA_TOKEN_USER_DATA_MYSQL)){

                prepStmt1.setString(1, mfaTokenDO.getMfaTokenId());
                prepStmt1.setString(2, mfaTokenDO.getMfaToken());
                prepStmt1.setString(3, mfaTokenDO.getMfaTokenState());
                prepStmt1.setLong(4, mfaTokenDO.getGeneratedTime());
                prepStmt1.setLong(5, mfaTokenDO.getExpiryTime());
                prepStmt1.setString(6, String.valueOf(mfaTokenDO.isAuthFlowCompleted()));
                prepStmt1.setInt(7, mfaTokenDO.getServiceProviderAppId());
                prepStmt1.setInt(8, mfaTokenDO.getSpTenantId());

                prepStmt1.execute();

                prepStmt2.setInt(1, mfaTokenDO.getUserTenantId());
                prepStmt2.setString(2, mfaTokenDO.getUserId());
                prepStmt2.setString(3, mfaTokenDO.getFullQualifiedUserName());
                prepStmt2.setString(4, mfaTokenDO.getMfaTokenId());
                prepStmt2.execute();

                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new MFAAuthServerException("Error while inserting mfa token data to the database.", e);
            }

        } catch (SQLException e) {
            throw new MFAAuthServerException("Error while closing connection after inserting MFA Token data to " +
                    "the database.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MFATokenDO getMFATokenData(String mfaToken) throws MFAAuthException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.RETRIEVE_MFA_TOKEN_DATA_MYSQL)) {
            prepStmt.setString(1, mfaToken);

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                if (resultSet.next()) {

                    MFATokenDO mfaTokenDO = new MFATokenDO();
                    mfaTokenDO.setUserId(resultSet.getString(Constants.DB_FIELD_USER_ID));
                    mfaTokenDO.setFullQualifiedUserName(resultSet.getString(Constants.DB_FIELD_USERNAME));
                    mfaTokenDO.setUserTenantId(resultSet.getInt(Constants.DB_FIELD_USER_TENANT_ID));
                    mfaTokenDO.setMfaTokenId(resultSet.getString(Constants.DB_FIELD_MFA_TOKEN_ID));
                    mfaTokenDO.setMfaToken(resultSet.getString(Constants.DB_FIELD_MFA_TOKEN));
                    mfaTokenDO.setSpTenantId(resultSet.getInt(Constants.DB_FIELD_SP_TENANT_ID));
                    mfaTokenDO.setMfaTokenState(resultSet.getString(Constants.DB_FIELD_TOKEN_STATE));
                    mfaTokenDO.setGeneratedTime(resultSet.getLong(Constants.DB_FIELD_TIME_GENERATED));
                    mfaTokenDO.setExpiryTime(resultSet.getLong(Constants.DB_FIELD_EXPIRY_TIME));
                    mfaTokenDO.setAuthFlowCompleted(resultSet.getBoolean(Constants.DB_FIELD_IS_AUTH_FLOW_COMPLETED));
                    mfaTokenDO.setServiceProviderAppId(resultSet.getInt(Constants.DB_FIELD_SP_APP_ID));
                    mfaTokenDO.setAuthenticatedSteps(getAuthenticatedSteps(connection, mfaTokenDO.getMfaTokenId()));

                    return mfaTokenDO;
                }
            }
        } catch (SQLException e) {
            throw new MFAAuthServerException("Error while retrieving mfa token data.", e);
        }

        throw Util.handleClientException(
                Constants.ErrorMessage.CLIENT_INVALID_MFA_TOKEN, mfaToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshMfaToken(String prevMfaTokenId, String prevMfaToken, MFATokenDO mfaTokenDO) throws MFAAuthException {
        updateMfaTokenState(prevMfaToken, Constants.MFA_TOKEN_STATE_INACTIVE);
        addRefreshedMFATokenData(prevMfaTokenId, mfaTokenDO);
    }

    @Override
    public void updateMfaTokenState(String mfaTokenId, String tokenState) throws MFAAuthException {

    }

    @Override
    public void addAuthenticatedStep(int stepNo, String authenticator, String mfaTokenId) throws MFAAuthException {

    }

    public void addRefreshedMFATokenData(String prevMfaTokenId, MFATokenDO mfaTokenDO) throws MFAAuthException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(true)){
            if (log.isDebugEnabled()) {
                log.debug("Adding refreshed MFA token data to the database.");
            }

            try (PreparedStatement prepStmt1 = connection.prepareStatement(SQLQueries.ADD_MFA_TOKEN_MYSQL);
                 PreparedStatement prepStmt2 =
                         connection.prepareStatement(SQLQueries.REFRESH_MFA_TOKEN_ID_MYSQL)){

                prepStmt1.setString(1, mfaTokenDO.getMfaTokenId());
                prepStmt1.setString(2, mfaTokenDO.getMfaToken());
                prepStmt1.setString(3, mfaTokenDO.getMfaTokenState());
                prepStmt1.setLong(4, mfaTokenDO.getGeneratedTime());
                prepStmt1.setLong(5, mfaTokenDO.getExpiryTime());
                prepStmt1.setString(6, String.valueOf(mfaTokenDO.isAuthFlowCompleted()));
                prepStmt1.setInt(7, mfaTokenDO.getServiceProviderAppId());
                prepStmt1.setInt(8, mfaTokenDO.getSpTenantId());

                prepStmt1.execute();

                prepStmt2.setString(1, mfaTokenDO.getMfaTokenId());
                prepStmt2.setString(2, mfaTokenDO.getMfaTokenId());
                prepStmt2.setString(3, prevMfaTokenId);

                prepStmt2.execute();

                IdentityDatabaseUtil.commitTransaction(connection);

            } catch (SQLException e) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw new MFAAuthServerException("Error while adding refreshed mfa token data to the database.",
                        e);
            }

        } catch (SQLException e) {
            throw new MFAAuthServerException("Error while closing connection after adding refreshed MFA Token data to " +
                    "the database.", e);
        }

    }

    public LinkedHashMap<Integer, String> getAuthenticatedSteps(Connection connection, String mfaTokenId) throws MFAAuthException {
        try (PreparedStatement prepStmt = connection.prepareStatement(SQLQueries.GET_AUTHENTICATED_STEPS_MYSQL)) {
            prepStmt.setString(1, mfaTokenId);

            LinkedHashMap<Integer, String> authenticatedSteps = new LinkedHashMap<>();

            try (ResultSet resultSet = prepStmt.executeQuery()) {
                while (resultSet.next()) {
                    authenticatedSteps.put(resultSet.getInt(Constants.DB_FIELD_DATA_KEY),
                            resultSet.getString(Constants.DB_FIELD_DATA_VALUE));
                }
                return authenticatedSteps;
            }
        } catch (SQLException e) {
            throw new MFAAuthServerException("Error while retrieving authenticated steps.", e);
        }
    }
}
