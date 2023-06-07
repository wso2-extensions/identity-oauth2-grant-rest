package org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache;
import org.wso2.carbon.identity.core.cache.BaseCache;
public class MFAAuthCache extends BaseCache<MFAAuthCacheKey, MFAAuthCacheEntry>{
    public static final String MFA_AUTH_CACHE_NAME = "MFAAuthCache";
    private static volatile MFAAuthCache instance;
    private MFAAuthCache() {super(MFA_AUTH_CACHE_NAME);}

    public static MFAAuthCache getInstance(){
        if (instance == null) {
            synchronized (MFAAuthCache.class) {
                if (instance == null) {
                    instance = new MFAAuthCache();
                }
            }
        }
        return instance;
    }
}
