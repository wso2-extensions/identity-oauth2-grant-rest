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

package org.wso2.carbon.identity.oauth2.grant.rest.handler;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.validators.AbstractValidator;
import javax.servlet.http.HttpServletRequest;

/**
 * Grant validator for REST OAuth2 Grant.
 * REST OAuth2 Grant request should have the required parameters -
 * grant_type, username and flowId.
 */
public class AuthenticationGrantValidator extends AbstractValidator<HttpServletRequest> {

    public AuthenticationGrantValidator() {

        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        requiredParams.add(AuthenticationGrantConstants.FLOW_ID_PARAM_PASSWORD_GRANT);
    }
}
