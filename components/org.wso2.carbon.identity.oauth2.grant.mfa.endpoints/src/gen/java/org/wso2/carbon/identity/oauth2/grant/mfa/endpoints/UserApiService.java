package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints;

import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.UserAuthenticationRequest;

import javax.ws.rs.core.Response;

public interface UserApiService {
    public Response userAuthenticatePost(UserAuthenticationRequest userAuthenticationRequest, String userTenantDomain);
}
