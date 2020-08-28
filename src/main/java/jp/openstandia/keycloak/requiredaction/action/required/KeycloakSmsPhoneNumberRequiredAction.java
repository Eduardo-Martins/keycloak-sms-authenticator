package jp.openstandia.keycloak.requiredaction.action.required;

import org.jboss.logging.Logger;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;

import jp.openstandia.keycloak.authenticator.SMSAuthContstants;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class KeycloakSmsPhoneNumberRequiredAction implements RequiredActionProvider {
    private static Logger logger = Logger.getLogger(KeycloakSmsPhoneNumberRequiredAction.class);
    public static final String PROVIDER_ID = "sms_auth_check_mobile";

    public void evaluateTriggers(RequiredActionContext context) {
        logger.debug("evaluateTriggers called ...");
    }



    public void requiredActionChallenge(RequiredActionContext context) {
        logger.debug("requiredActionChallenge called ...");

        UserModel user = context.getUser();
        String mobileNumber = getPhoneNumber(user);

        Response challenge = context.form()
                .setAttribute("phoneNumber", mobileNumber)
                .createForm("sms-validation-mobile-number.ftl");
        context.challenge(challenge);
    }
    
    private String getPhoneNumber(UserModel user) {
		List<String> phoneNumberList = user.getAttribute(SMSAuthContstants.ATTR_PHONE_NUMBER);
		if (phoneNumberList != null && !phoneNumberList.isEmpty()) {
			return phoneNumberList.get(0);
		}
		return null;
	}

    public void processAction(RequiredActionContext context) {
        logger.debug("processAction called ...");

        String answer = (context.getHttpRequest().getDecodedFormParameters().getFirst("mobile_number"));
        if (answer != null && answer.length() > 0) {
            logger.debug("Valid matching mobile numbers supplied, save credential ...");
            List<String> mobileNumber = new ArrayList();
            mobileNumber.add(answer);

            UserModel user = context.getUser();
            user.setAttribute(SMSAuthContstants.ATTR_PHONE_NUMBER, mobileNumber);

            context.success();
        } else {
            logger.debug("The field wasn\'t complete or is an invalid number...");
            Response challenge = context.form()
                    .setError("mobile_number.no.valid")
                    .createForm("sms-validation-mobile-number.ftl");
            context.challenge(challenge);
        }
    }

    public void close() {
        logger.debug("close called ...");
    }
}
