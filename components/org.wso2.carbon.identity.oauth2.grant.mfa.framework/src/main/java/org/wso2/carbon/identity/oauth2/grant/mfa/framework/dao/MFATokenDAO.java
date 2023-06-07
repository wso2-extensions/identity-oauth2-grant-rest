package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao;
import  org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

public interface MFATokenDAO {
    /**
     * This method is to insert MFA Token Data.
     *
     * @param MFATokenDO 	MFA Token Data Object.
     * @throws MFAAuthException if failed to insert MFA Token data.
     */
    public void addMFATokenData(MFATokenDO MFATokenDO) throws MFAAuthException;

    /**
     * This method is to retrieve MFA Token Data.
     *
     * @param mfaToken		UUID to track the flow.
     * @throws MFAAuthException if failed to retrieve MFA Token data.
     */
    public MFATokenDO getMFATokenData(String mfaToken) throws MFAAuthException;

    /**
     * This method is to renew the MFA Token.
     *
     * @param prevMfaToken 		previous MFA Token.
     * @param mfaTokenDO 		MFA Token data object with a new MFA Token and a new MFA Token Id.
     * @throws MFAAuthException if failed to renew the MFA Token.
     */
    public void refreshMfaToken (String prevMfaTokenId, String prevMfaToken, MFATokenDO mfaTokenDO) throws MFAAuthException;

    /**
     * This method is to update MFA Token State.
     *
     * @param mfaTokenId 	UUID to uniquely identify the MFA Token and related data.
     * @param tokenState 	MFA Token State
     * @throws MFAAuthException if failed to update MFA Token State.
     */
    public void updateMfaTokenState (String mfaTokenId, String tokenState) throws MFAAuthException;

    /**
     * This method is to add a successfully authenticated step to the DB.
     *
     * @param stepNo 	Authentication Step Number.
     * @param authenticator 	Authenticator name
     * @param mfaTokenId 	UUID to uniquely identify the MFA Token and related data
     * @throws MFAAuthException if failed to add authenticated step.
     */
    public void addAuthenticatedStep (int stepNo, String authenticator, String mfaTokenId ) throws MFAAuthException;

}
