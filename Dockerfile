FROM jboss/keycloak:6.0.1
ADD ./target/sms-authenticator.jar /opt/jboss/keycloak/providers/sms-authenticator.jar
ADD ./themes/openstandia/ /opt/jboss/keycloak/themes/ 

USER root
RUN chown -R jboss:root /opt/jboss/keycloak/providers/ &&\
    chmod +x /opt/jboss/keycloak/providers/sms-authenticator.jar
USER jboss
