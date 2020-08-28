package jp.openstandia.keycloak.requiredaction.credential;

import org.keycloak.Config;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class KeycloakSmsPhoneNumberCredentialProviderFactory implements CredentialProviderFactory<KeycloakSmsPhoneNumberCredentialProvider> {
    @Override
    public String getId() {
        return "phoneNumber";
    }

    @Override
    public CredentialProvider create(KeycloakSession session) {
        return new KeycloakSmsPhoneNumberCredentialProvider(session);
    }
    
    @Override
    public void close() {

    }
    
    public void postInit(KeycloakSessionFactory factory) {
    	
    }
    

    public void init(Config.Scope config) {
    	
    }
    
}
