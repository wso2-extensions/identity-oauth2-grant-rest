package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache.MFAAuthCache;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache.MFAAuthCacheEntry;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache.MFAAuthCacheKey;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.exception.MFAAuthException;

public class CacheBackedMFATokenDAO extends MFATokenDAOImpl {

    private static final Log log = LogFactory.getLog(CacheBackedMFATokenDAO.class);
    private static MFATokenDAO mfaTokenDAO;
    private static MFAAuthCache mfaAuthCache = null;
    private static volatile CacheBackedMFATokenDAO instance;

    public static CacheBackedMFATokenDAO getInstance() {
        if (instance == null) {
            synchronized (CacheBackedMFATokenDAO.class) {
                if (instance == null) {
                    instance = new CacheBackedMFATokenDAO(MFATokenDAOImpl.getInstance());
                }
            }
        }
        return instance;
    }

    public CacheBackedMFATokenDAO(MFATokenDAO mfaTokenDAO) {

        this.mfaTokenDAO = mfaTokenDAO;
        mfaAuthCache = MFAAuthCache.getInstance();
    }

    private void addToCache(MFATokenDO mfaTokenDO, String tenantDomain) {

        if (log.isDebugEnabled()) {
            log.debug("Add cache for the MFA authentication flow " + mfaTokenDO.getMfaToken() + "@" + tenantDomain);
        }

        MFAAuthCacheKey mfaTokenKey = new MFAAuthCacheKey(mfaTokenDO.getMfaToken());
        MFAAuthCacheEntry mfaTokenEntry = new MFAAuthCacheEntry(mfaTokenDO);
        mfaAuthCache.addToCache(mfaTokenKey, mfaTokenEntry, tenantDomain);
    }

    @Override
    public void addMFATokenData(MFATokenDO mfaTokenDO) throws MFAAuthException {
        String authenticator = mfaTokenDO.getAuthenticatedSteps().get(mfaTokenDO.getAuthenticatedSteps().size());

        mfaTokenDAO.addMFATokenData(mfaTokenDO);
        addToCache(mfaTokenDO, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        mfaTokenDAO.addAuthenticatedStep(mfaTokenDO.getAuthenticatedSteps().size(), authenticator,
                mfaTokenDO.getMfaTokenId());
    }

    @Override
    public void refreshMfaToken(String prevMfaTokenId, String prevMfaToken, MFATokenDO mfaTokenDO) throws MFAAuthException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        clearMfaTokenCache(prevMfaToken, tenantDomain);
        mfaTokenDAO.refreshMfaToken(prevMfaTokenId, prevMfaToken, mfaTokenDO);
        addToCache(mfaTokenDO, tenantDomain);

    }

    @Override
    public void updateMfaTokenState(String mfaToken, String tokenState) throws MFAAuthException {

        MFATokenDO mfaTokenDO = getMFATokenData(mfaToken);
        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        clearMfaTokenCache(mfaToken, tenantDomain);
        mfaTokenDAO.updateMfaTokenState(mfaToken, tokenState);
        addToCache(mfaTokenDO, tenantDomain);

    }

    @Override
    public MFATokenDO getMFATokenData(String mfaToken) throws MFAAuthException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        MFATokenDO mfaTokenDO = getMFATokenDataFromCache(mfaToken, tenantDomain);
        if (mfaTokenDO == null) {
            // Cache miss, fetch from DB.
            mfaTokenDO = mfaTokenDAO.getMFATokenData(mfaToken);
            addToCache(mfaTokenDO, tenantDomain);
        }

        return mfaTokenDO;
    }

    private MFATokenDO getMFATokenDataFromCache(String mfaToken, String tenantDomain) {

        MFATokenDO mfaTokenDO = null;
        MFAAuthCacheKey cacheKey = new MFAAuthCacheKey(mfaToken);
        MFAAuthCacheEntry entry = mfaAuthCache.getValueFromCache(cacheKey, tenantDomain);

        if (entry != null) {
            mfaTokenDO = entry.getMFATokenDO();
        }
        if (mfaTokenDO == null) {
            if (log.isDebugEnabled()) {
                log.debug("Cache missing for the MFA authentication flow with MFA Token: " + mfaToken);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Cache present for the MFA authentication flow with MFA Token: " + mfaToken);
            }
        }
        return mfaTokenDO;
    }

    public void clearMFATokenDataFromCache(String mfaToken, String tenantDomain) {

        clearMfaTokenCache(mfaToken, tenantDomain);
    }

    public void clearMfaTokenCache(String mfaToken, String tenantDomain) {

        if (log.isDebugEnabled()) {
            log.debug("Clearing all the MFA Token Caches for " + mfaToken + "@" +
                    tenantDomain);
        }

        MFAAuthCacheKey cacheKey = new MFAAuthCacheKey(mfaToken);
        mfaAuthCache.clearCacheEntry(cacheKey, tenantDomain);

    }
}
