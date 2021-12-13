package com.sidorovich.pavel.buber.core.mail;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MailSenderTest {

    private final MailSender mailSender = new MailSender("123", "Subject", "Hi");

    @Test
    public void send_shouldNotSendEmail_whenEmailIsInvalid() {
        mailSender.send();
    }

}