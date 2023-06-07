package org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache;

import org.wso2.carbon.identity.core.cache.CacheEntry;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao.MFATokenDO;

public class MFAAuthCacheEntry extends CacheEntry {

    private static final long serialVersionUID = 3112605038259278777L;

    private MFATokenDO mfaTokenDO;

    public MFAAuthCacheEntry(MFATokenDO mfaTokenDO) {

        this.mfaTokenDO = mfaTokenDO;
    }

    public MFATokenDO getMFATokenDO() {

        return mfaTokenDO;
    }

    public void setMFATokenDO(MFATokenDO mfaTokenDO) {

        this.mfaTokenDO = mfaTokenDO;
    }
}
