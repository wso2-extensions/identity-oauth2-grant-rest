package org.wso2.carbon.identity.oauth2.grant.mfa.framework.internal;

import org.wso2.carbon.extension.identity.emailotp.common.EmailOtpService;
import org.wso2.carbon.identity.multi.attribute.login.mgt.MultiAttributeLoginService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.MFAAuthListenerService;
import org.wso2.carbon.identity.oauth2.grant.mfa.framework.dto.ConfigsDTO;
import org.wso2.carbon.identity.smsotp.common.SMSOTPService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Data holder for MFA Authentication Service.
 */
public class MFAAuthServiceDataHolder {

	private static final MFAAuthServiceDataHolder dataHolder = new MFAAuthServiceDataHolder();
	private RealmService realmService;
	private SMSOTPService smsOtpService;
	private EmailOtpService emailOtpService;
	private MFAAuthListenerService mfaAuthListenerService;
	private MultiAttributeLoginService multiAttributeLoginService;
	private static final ConfigsDTO configs = new ConfigsDTO();


	public static MFAAuthServiceDataHolder getInstance() {

		return dataHolder;
	}

	public RealmService getRealmService() {

		return realmService;
	}

	public void setRealmService(RealmService realmService) {

		this.realmService = realmService;
	}

	public SMSOTPService getSMSOtpService() {

		return smsOtpService;
	}

	public void setSMSOtpService(SMSOTPService smsOtpService) {

		this.smsOtpService = smsOtpService;
	}


	public EmailOtpService getEmailOtpService() {

		return emailOtpService;
	}

	public void setEmailOtpService(EmailOtpService emailOtpService) {

		this.emailOtpService = emailOtpService;
	}

	public MFAAuthListenerService getMFAAuthListenerService() {

		return mfaAuthListenerService;
	}

	public void setMFAAuthListenerService(MFAAuthListenerService mfaAuthListenerService) {

		this.mfaAuthListenerService = mfaAuthListenerService;
	}

	public MultiAttributeLoginService getMultiAttributeLogin() {

		return multiAttributeLoginService;
	}

	public void setMultiAttributeLogin(MultiAttributeLoginService multiAttributeLoginService) {

		this.multiAttributeLoginService = multiAttributeLoginService;
	}

	public static ConfigsDTO getConfigs() {

		return configs;
	}

}
