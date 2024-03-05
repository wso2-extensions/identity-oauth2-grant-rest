/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.oauth2.grant.rest.core.cache.AuthCache;
import org.wso2.carbon.identity.oauth2.grant.rest.core.cache.AuthCacheEntry;
import org.wso2.carbon.identity.oauth2.grant.rest.core.cache.AuthCacheKey;
import org.wso2.carbon.identity.oauth2.grant.rest.core.exception.AuthenticationException;

/**
 * This class is used to function the caching in API  based REST authentication flow.
 */
public class CacheBackedFlowIdDAO extends FlowIdDAOImpl {

    private static final Log LOG = LogFactory.getLog(CacheBackedFlowIdDAO.class);
    private static FlowIdDAO flowIdDAO;
    private static AuthCache restAuthCache = null;
    private static volatile CacheBackedFlowIdDAO instance;

    public static CacheBackedFlowIdDAO getInstance() {
        if (instance == null) {
            synchronized (CacheBackedFlowIdDAO.class) {
                if (instance == null) {
                    instance = new CacheBackedFlowIdDAO(FlowIdDAOImpl.getInstance());
                }
            }
        }
        return instance;
    }

    public CacheBackedFlowIdDAO(FlowIdDAO flowIdDAO) {

        this.flowIdDAO = flowIdDAO;
        restAuthCache = AuthCache.getInstance();
    }

    private void addToCache(FlowIdDO flowIdDO, String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Add cache for the REST authentication flow " + flowIdDO.getFlowId() + "@" + tenantDomain);
        }

        AuthCacheKey flowIdKey = new AuthCacheKey(flowIdDO.getFlowId());
        AuthCacheEntry flowIdEntry = new AuthCacheEntry(flowIdDO);
        restAuthCache.addToCache(flowIdKey, flowIdEntry);
    }

    @Override
    public void addFlowIdData(FlowIdDO flowIdDO) throws AuthenticationException {
        String authenticator = flowIdDO.getAuthenticatedSteps().get(flowIdDO.getAuthenticatedSteps().size());
        flowIdDAO.addFlowIdData(flowIdDO);
        addToCache(flowIdDO, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        flowIdDAO.addAuthenticatedStep(flowIdDO.getAuthenticatedSteps().size(), authenticator,
                flowIdDO.getFlowIdIdentifier());
    }

    @Override
    public void refreshFlowId(String previousFlowIdIdentifier, String prevFlowId, FlowIdDO flowIdDO)
            throws AuthenticationException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        clearFlowIdCache(prevFlowId, tenantDomain);
        flowIdDAO.refreshFlowId(previousFlowIdIdentifier, prevFlowId, flowIdDO);
        addToCache(flowIdDO, tenantDomain);

    }

    @Override
    public void updateFlowIdState(String flowIdIdentifier, String flowIdState) throws AuthenticationException {

        FlowIdDO flowIdDO = getFlowIdData(flowIdIdentifier);
        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        clearFlowIdCache(flowIdIdentifier, tenantDomain);
        flowIdDAO.updateFlowIdState(flowIdIdentifier, flowIdState);
        flowIdDO.setFlowIdState(flowIdState);
        addToCache(flowIdDO, tenantDomain);

    }

    @Override
    public FlowIdDO getFlowIdData(String flowId) throws AuthenticationException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        FlowIdDO flowIdDO = getFlowIdDataFromCache(flowId, tenantDomain);
        if (flowIdDO == null) {
            // Cache miss, fetch from DB.
            flowIdDO = flowIdDAO.getFlowIdData(flowId);
            addToCache(flowIdDO, tenantDomain);
        }

        return flowIdDO;
    }

    private FlowIdDO getFlowIdDataFromCache(String flowId, String tenantDomain) {

        FlowIdDO flowIdDO = null;
        AuthCacheKey cacheKey = new AuthCacheKey(flowId);
        AuthCacheEntry entry = restAuthCache.getValueFromCache(cacheKey);

        if (entry != null) {
            flowIdDO = entry.getFlowIdDO();
        }
        if (flowIdDO == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cache missing for the REST authentication flow with Flow ID: " + flowId);
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cache present for the REST authentication flow with Flow ID: " + flowId);
            }
        }
        return flowIdDO;
    }

    public void clearFlowIdCache(String flowId, String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Clearing all the Flow ID Caches for " + flowId + "@" +
                    tenantDomain);
        }

        AuthCacheKey cacheKey = new AuthCacheKey(flowId);
        restAuthCache.clearCacheEntry(cacheKey);
    }
}
