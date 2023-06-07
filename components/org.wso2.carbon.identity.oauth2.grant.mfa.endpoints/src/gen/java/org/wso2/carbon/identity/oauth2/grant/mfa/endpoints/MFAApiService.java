package org.wso2.carbon.identity.oauth2.grant.mfa.endpoints;

import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAInitializationRequest;
import org.wso2.carbon.identity.oauth2.grant.mfa.endpoints.dto.MFAValidationRequest;

import javax.ws.rs.core.Response;

public interface MFAApiService {
    public Response mfaInitializePost(MFAInitializationRequest mfaInitializationRequest);
    public Response mfaValidatePost(MFAValidationRequest mfaValidationRequest);
}
