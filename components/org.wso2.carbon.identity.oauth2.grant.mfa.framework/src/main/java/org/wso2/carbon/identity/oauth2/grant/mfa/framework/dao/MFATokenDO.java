package org.wso2.carbon.identity.oauth2.grant.mfa.framework.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * MFA Token data object model.
 */
public class MFATokenDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String fullQualifiedUserName;
    private String mfaTokenId;
    private String mfaToken;
    private String mfaTokenState;
    private long generatedTime;
    private long expiryTime;
    private boolean isAuthFlowCompleted;
    private int serviceProviderAppId;
    private int spTenantId;
    private int userTenantId;
    private LinkedHashMap<Integer, String> authenticatedSteps;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullQualifiedUserName() {
        return fullQualifiedUserName;
    }

    public void setFullQualifiedUserName(String fullQualifiedUserName) {
        this.fullQualifiedUserName = fullQualifiedUserName;
    }

    public String getMfaTokenId() {
        return mfaTokenId;
    }

    public void setMfaTokenId(String mfaTokenId) {
        this.mfaTokenId = mfaTokenId;
    }

    public String getMfaToken() {
        return mfaToken;
    }

    public void setMfaToken(String mfaToken) {
        this.mfaToken = mfaToken;
    }

    public String getMfaTokenState() {
        return mfaTokenState;
    }

    public void setMfaTokenState(String mfaTokenState) {
        this.mfaTokenState = mfaTokenState;
    }

    public long getGeneratedTime() { return generatedTime; }

    public void setGeneratedTime(long generatedTime) { this.generatedTime = generatedTime; }

    public long getExpiryTime() { return expiryTime; }

    public void setExpiryTime(long expiryTime) { this.expiryTime = expiryTime; }

    public boolean isAuthFlowCompleted () {
        return isAuthFlowCompleted;
    }

    public void setAuthFlowCompleted (boolean isAuthFlowCompleted) {
        this.isAuthFlowCompleted = isAuthFlowCompleted;
    }

    public int getServiceProviderAppId() {
        return serviceProviderAppId;
    }

    public void setServiceProviderAppId(int serviceProviderAppId) {
        this.serviceProviderAppId = serviceProviderAppId;
    }

    public LinkedHashMap<Integer, String> getAuthenticatedSteps() {
        return authenticatedSteps;
    }

    public int getSpTenantId() { return spTenantId; }

    public void setSpTenantId(int spTenantId) {
        this.spTenantId = spTenantId;
    }

    public int getUserTenantId() {
        return userTenantId;
    }

    public void setUserTenantId(int userTenantId) {
        this.userTenantId = userTenantId;
    }

    public void setAuthenticatedSteps(LinkedHashMap<Integer, String> authenticatedSteps) {
        this.authenticatedSteps = authenticatedSteps;
    }
}
