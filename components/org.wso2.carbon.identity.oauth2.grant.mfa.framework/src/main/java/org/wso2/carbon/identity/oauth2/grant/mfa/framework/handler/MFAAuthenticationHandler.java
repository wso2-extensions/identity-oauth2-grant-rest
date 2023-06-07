package org.wso2.carbon.identity.oauth2.grant.mfa.framework.handler;

import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.core.handler.InitConfig;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.base.IdentityRuntimeException;

public class MFAAuthenticationHandler extends AbstractEventHandler {

    @Override
    public String getName() {

        return "";
    }

    @Override
    public void handleEvent(Event event) throws IdentityEventException {

    }

    @Override
    public void init(InitConfig configuration) throws IdentityRuntimeException {

        super.init(configuration);
    }

    @Override
    public int getPriority(MessageContext messageContext) {

        return 250;
    }
}
