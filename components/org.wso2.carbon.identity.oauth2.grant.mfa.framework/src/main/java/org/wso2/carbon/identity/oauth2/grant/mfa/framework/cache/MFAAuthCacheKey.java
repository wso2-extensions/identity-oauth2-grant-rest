package org.wso2.carbon.identity.oauth2.grant.mfa.framework.cache;

import org.wso2.carbon.identity.core.cache.CacheKey;

public class MFAAuthCacheKey extends CacheKey {

    private static final long serialVersionUID = 8263255365985309443L;

    private String mfaTokenKey;

    /**
     * @param mfaToken
     */
    public MFAAuthCacheKey(String mfaToken) {

        this.mfaTokenKey = mfaToken;
    }

    /**
     * @return
     */
    public String getMfaTokenKey() {

        return mfaTokenKey;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }

        MFAAuthCacheKey authCacheKey = (MFAAuthCacheKey) object;

        return mfaTokenKey.equals(authCacheKey.mfaTokenKey);
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + mfaTokenKey.hashCode();
        return result;
    }
}
