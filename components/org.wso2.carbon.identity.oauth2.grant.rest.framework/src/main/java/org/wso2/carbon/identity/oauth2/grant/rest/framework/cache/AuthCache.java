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

package org.wso2.carbon.identity.oauth2.grant.rest.framework.cache;
import org.wso2.carbon.identity.core.cache.BaseCache;

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
