package com.uk.uk.implementation;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendSimpleEmail(String to, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("info@sipsite.co.uk");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true indicates HTML

        javaMailSender.send(message);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("test@testweb.com");  // Your email address
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//
//        javaMailSender.send(message);
    }
}

