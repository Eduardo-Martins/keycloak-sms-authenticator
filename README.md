# keycloak-sms-authenticator twilio 

Twilio SMS Dockerized with KeyCloak 11.0.2 works with Keycloak REST API (Improved Code in Brasil):

    Go to Dockerfile path and simply docker build .  //////// voila!!!

Configure your REALM to use the SMS Authentication. First create a new REALM (or select a previously created REALM).

Under Authentication > Flows:

    Copy 'Browse' flow to 'sms-twilio browser' flow
    Click on 'Actions > Add execution on the 'sms-twilio Browser Forms' line and add the 'Twilio SMS Authentication'
    Set 'Twilio SMS Authentication' to 'REQUIRED'
    To configure the SMS Authernticator, click on Actions Config and fill in the attributes.

Under Authentication > Bindings:

    Select 'Browser with SMS' as the 'Browser Flow' for the REALM.
    

