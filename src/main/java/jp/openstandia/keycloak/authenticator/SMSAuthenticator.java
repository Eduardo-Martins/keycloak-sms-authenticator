package jp.openstandia.keycloak.authenticator;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import jp.openstandia.keycloak.authenticator.api.SMSSendVerify;

public class SMSAuthenticator implements Authenticator {

	private static final Logger logger = Logger.getLogger(SMSAuthenticator.class.getPackage().getName());

	public void authenticate(AuthenticationFlowContext context) {
		logger.debug("Method [authenticate]");

		AuthenticatorConfigModel config = context.getAuthenticatorConfig();

		UserModel user = context.getUser();
		String phoneNumber = getPhoneNumber(user);
		logger.debugv("phoneNumber : {0}", phoneNumber);
		
		String phoneVerified = getPhoneVerified(user);
		logger.debugv("verified : {0}", phoneVerified);
		
		if (phoneVerified.equals("1")) {
			logger.debugv("verified : {0}", phoneVerified);
			context.success();
		}

		else if (phoneNumber != null) {

			// SendSMS
			SMSSendVerify sendVerify = new SMSSendVerify(getConfigString(config, SMSAuthContstants.CONFIG_SMS_API_KEY),
					getConfigString(config, SMSAuthContstants.CONFIG_PROXY_FLAG),
					getConfigString(config, SMSAuthContstants.CONFIG_PROXY_URL),
					getConfigString(config, SMSAuthContstants.CONFIG_PROXY_PORT),
					getConfigString(config, SMSAuthContstants.CONFIG_CODE_LENGTH));
			
			MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
			String enteredCode = inputData.containsKey("smsCode") ? inputData.getFirst("smsCode") : "";
			
			if (!enteredCode.isEmpty() && sendVerify.verifySMS(phoneNumber, enteredCode)) {
				List<String> list = new ArrayList<>();
				list.add("1");
				user.setAttribute("phoneVerified", list);
				logger.info("verify code check : OK");
				context.success();

			}

			else if (sendVerify.sendSMS(phoneNumber)) {
				Response challenge = context.form().createForm("sms-validation.ftl");
				context.challenge(challenge);

			} else {
				Response challenge = context.form().addError(new FormMessage("sendSMSCodeErrorMessage"))
						.createForm("sms-validation-error.ftl");
				context.challenge(challenge);
			}

		} else {
			Response challenge = context.form().addError(new FormMessage("missingTelNumberMessage"))
					.createForm("sms-validation-error.ftl");
			context.challenge(challenge);
		}

	}

	public void action(AuthenticationFlowContext context) {
		logger.debug("Method [action]");

		MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
		String enteredCode = inputData.containsKey("smsCode") ? inputData.getFirst("smsCode") : "";

		UserModel user = context.getUser();
		String phoneNumber = getPhoneNumber(user);
		logger.debugv("phoneNumber : {0}", phoneNumber);
		
		String phoneVerified = getPhoneVerified(user);
		logger.debugv("verified : {0}", phoneVerified);

		// SendSMS
		AuthenticatorConfigModel config = context.getAuthenticatorConfig();
		SMSSendVerify sendVerify = new SMSSendVerify(getConfigString(config, SMSAuthContstants.CONFIG_SMS_API_KEY),
				getConfigString(config, SMSAuthContstants.CONFIG_PROXY_FLAG),
				getConfigString(config, SMSAuthContstants.CONFIG_PROXY_URL),
				getConfigString(config, SMSAuthContstants.CONFIG_PROXY_PORT),
				getConfigString(config, SMSAuthContstants.CONFIG_CODE_LENGTH));
		
		if (phoneVerified.equals("1")) {
			logger.info("phone is verified");
			context.success();
		}

		else if (!enteredCode.isEmpty() && sendVerify.verifySMS(phoneNumber, enteredCode)) {
			logger.info("verify code check : OK");
			context.success();

		} else {
			Response challenge = context.form()
					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
					.addError(new FormMessage("invalidSMSCodeMessage")).createForm("sms-validation-error.ftl");
			context.challenge(challenge);
		}

	}

	public boolean requiresUser() {
		logger.debug("Method [requiresUser]");
		return false;
	}

	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		logger.debug("Method [configuredFor]");
		return false;
	}

	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

	}

	public void close() {
		logger.debug("<<<<<<<<<<<<<<< SMSAuthenticator close");
	}

	private String getPhoneNumber(UserModel user) {
		List<String> phoneNumberList = user.getAttribute(SMSAuthContstants.ATTR_PHONE_NUMBER);
		if (phoneNumberList != null && !phoneNumberList.isEmpty()) {
			return phoneNumberList.get(0);
		}
		return null;
	}
	
	private String getPhoneVerified(UserModel user) {
		List<String> phoneVerifiedList = user.getAttribute(SMSAuthContstants.ATTR_PHONE_VERIFIED);
		if (phoneVerifiedList != null && !phoneVerifiedList.isEmpty()) {
			return phoneVerifiedList.get(0);
		}
		return null;
	}

	private String getConfigString(AuthenticatorConfigModel config, String configName) {
		String value = null;
		if (config.getConfig() != null) {
			// Get value
			value = config.getConfig().get(configName);
		}
		return value;
	}
}