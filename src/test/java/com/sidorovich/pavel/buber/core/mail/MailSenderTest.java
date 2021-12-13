package com.sidorovich.pavel.buber.core.mail;

import org.testng.annotations.Test;

public class MailSenderTest {

    private final MailSender mailSender = new MailSender("123", "Subject", "Hi");

    @Test
    public void send_shouldNotSendEmail_whenEmailIsInvalid() {
        mailSender.send();
    }

}