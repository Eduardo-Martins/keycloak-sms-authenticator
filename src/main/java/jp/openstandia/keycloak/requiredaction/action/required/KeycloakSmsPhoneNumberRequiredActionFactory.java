package jp.openstandia.keycloak.requiredaction.action.required;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;


public class KeycloakSmsPhoneNumberRequiredActionFactory implements RequiredActionFactory {
    private static Logger logger = Logger.getLogger(KeycloakSmsPhoneNumberRequiredActionFactory.class);
    private static final KeycloakSmsPhoneNumberRequiredAction SINGLETON = new KeycloakSmsPhoneNumberRequiredAction();

    public RequiredActionProvider create(KeycloakSession session) {
        logger.debug("create called ...");
        return SINGLETON;
    }

    public String getId() {
        logger.debug("getId called ... returning " + KeycloakSmsPhoneNumberRequiredAction.PROVIDER_ID);
        return KeycloakSmsPhoneNumberRequiredAction.PROVIDER_ID;
    }

    public String getDisplayText() {
        logger.debug("getDisplayText called ...");
        return "Update Mobile Number";
    }

    public void init(Config.Scope config) {
        logger.debug("init called ...");
    }

    public void postInit(KeycloakSessionFactory factory) {
        logger.debug("postInit called ...");
    }

    public void close() {
        logger.debug("getId close ...");
    }
}
