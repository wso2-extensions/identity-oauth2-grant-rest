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

package org.wso2.carbon.identity.oauth2.grant.rest.core.cache;
import org.wso2.carbon.identity.application.common.cache.BaseCache;

/**
 * This class is used to get a cache instance when request is being dispatched to same node.
 */
public class AuthCache extends BaseCache<AuthCacheKey, AuthCacheEntry> {
    public static final String AUTH_CACHE_NAME = "AuthCache";
    private static volatile AuthCache instance;
    private AuthCache() {

        super(AUTH_CACHE_NAME);
    }

    public static AuthCache getInstance() {

        if (instance == null) {
            synchronized (AuthCache.class) {
                if (instance == null) {
                    instance = new AuthCache();
                }
            }
        }
        return instance;
    }
}
