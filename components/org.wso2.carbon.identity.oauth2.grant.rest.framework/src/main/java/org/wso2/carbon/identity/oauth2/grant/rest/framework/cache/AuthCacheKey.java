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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.cache.CacheKey;

/**
 * This method is used to keep a cache identifier.
 */
public class AuthCacheKey extends CacheKey {

    private static final Log log = LogFactory.getLog(AuthCacheKey.class);
    private static final long serialVersionUID = 8263255365985309443L;

    private String flowIdKey;

    /**
     * @param flowId
     */
    public AuthCacheKey(String flowId) {

        this.flowIdKey = flowId;
    }

    /**
     * @return
     */
    public String getFlowIdKey() {

        return flowIdKey;
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

        AuthCacheKey authCacheKey = (AuthCacheKey) object;

        return flowIdKey.equals(authCacheKey.flowIdKey);
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + flowIdKey.hashCode();
        return result;
    }
}
