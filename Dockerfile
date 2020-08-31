FROM jboss/keycloak:11.0.2
ADD ./target/sms-authenticator.jar /opt/jboss/keycloak/standalone/deployments/sms-authenticator.jar
ADD ./themes/sms-twilio /opt/jboss/keycloak/themes/sms-twilio/ 

USER root
RUN chown -R jboss:root /opt/jboss/keycloak/standalone/deployments/ &&\
    chown -R jboss:root /opt/jboss/keycloak/themes/ &&\
    chmod +x /opt/jboss/keycloak/standalone/deployments/sms-authenticator.jar
USER jboss
