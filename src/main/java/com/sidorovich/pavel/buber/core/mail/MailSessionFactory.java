package com.sidorovich.pavel.buber.core.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class MailSessionFactory {

    private static final String MAIL_USER_NAME_PROPERTY_NAME = "mail.user.name";
    private static final String MAIL_USER_PASSWORD_PROPERTY_NAME = "mail.user.password";

    private MailSessionFactory(){
    }

    public Session createSession(Properties configProperties) {
        final String userName = configProperties.getProperty(MAIL_USER_NAME_PROPERTY_NAME);
        final String userPassword = configProperties.getProperty(MAIL_USER_PASSWORD_PROPERTY_NAME);

        return Session.getDefaultInstance(
                configProperties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, userPassword);
                    }
                }
        );
    }

    public static MailSessionFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final MailSessionFactory INSTANCE = new MailSessionFactory();
    }

}
