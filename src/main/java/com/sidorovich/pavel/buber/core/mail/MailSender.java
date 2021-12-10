package com.sidorovich.pavel.buber.core.mail;

import com.sidorovich.pavel.buber.core.util.PropertyWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class MailSender {

    private static final Logger LOG = LogManager.getLogger(MailSender.class);
    private static final String TEXT_HTML_TYPE = "text/html";
    private static final String INVALID_ADDRESS_MSG = "Invalid address: %s";
    private static final String SENDING_MESSAGE_ERROR_MSG = "Error generating or sending message";
    private static final String CANNOT_FIND_PROPERTY_FILE_MSG = "Cannot find property file";
    private static final String MAIL_PROPERTIES_FILE_NAME = "mail.properties";

    private final MailSessionFactory sessionFactory;
    private final String sendToEmail;
    private final String mailSubject;
    private final String mailText;

    private MimeMessage message;

    public MailSender(String email, String mailSubject, String mailText) {
        this.sendToEmail = email;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        sessionFactory = MailSessionFactory.getInstance();
    }

    public void send() {
        try {
            prepareMail();
            Transport.send(message); // sending mail
        } catch (AddressException e) {
            LOG.error(String.format(INVALID_ADDRESS_MSG, sendToEmail), e);
        } catch (MessagingException e) {
            LOG.error(SENDING_MESSAGE_ERROR_MSG, e);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private void prepareMail() throws MessagingException {
        URL url = getClass().getClassLoader().getResource(MAIL_PROPERTIES_FILE_NAME);

        if (url != null) {
            try {
                final Properties properties = new PropertyWrapper(url.getPath());
                final Session mailSession = sessionFactory.createSession(properties);

                initMail(mailSession);
            } catch (IOException e) {
                throw new MessagingException(CANNOT_FIND_PROPERTY_FILE_MSG, e);
            }
        }
    }

    private void initMail(Session mailSession) throws MessagingException {
        mailSession.setDebug(true);
        message = new MimeMessage(mailSession); // create a mailing object
        // loading parameters into the mail message object
        message.setSubject(mailSubject);
        message.setContent(mailText, TEXT_HTML_TYPE);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
    }
}
