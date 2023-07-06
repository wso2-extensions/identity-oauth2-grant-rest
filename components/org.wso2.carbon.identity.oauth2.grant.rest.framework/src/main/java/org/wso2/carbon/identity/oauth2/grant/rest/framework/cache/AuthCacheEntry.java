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

import org.wso2.carbon.identity.core.cache.CacheEntry;
import org.wso2.carbon.identity.oauth2.grant.rest.framework.dao.FlowIdDO;

/**
 * This class is used to keep a cache entry to rest authentication through the flowId.
 */
public class AuthCacheEntry extends CacheEntry {

    private static final long serialVersionUID = 3112605038259278777L;

    private FlowIdDO flowIdDO;

    public AuthCacheEntry(FlowIdDO flowIdDO) {

        this.flowIdDO = flowIdDO;
    }

    public FlowIdDO getFlowIdDO() {

        return flowIdDO;
    }

    public void setFlowIdDO(FlowIdDO flowIdDO) {

        this.flowIdDO = flowIdDO;
    }
}
