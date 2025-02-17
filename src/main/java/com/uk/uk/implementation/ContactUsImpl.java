package com.uk.uk.implementation;

import com.uk.uk.entity.ContactUsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactUsImpl {

    @Autowired
    private JavaMailSender javaMailSender;

    public void contactUsSendEmail(ContactUsDAO contactUsDAORequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("info@sipsite.co.uk");
            message.setTo("info@sipsite.co.uk");

            String userEmailId = "Anonymous";
            if (!contactUsDAORequest.getEmailId().isEmpty())
                userEmailId = contactUsDAORequest.getEmailId();

            String subject = "Message From " + userEmailId;
            if (!contactUsDAORequest.getContactNo().isEmpty())
                subject += "(" + contactUsDAORequest.getContactNo() + ")";

            message.setSubject(subject);
            message.setText(contactUsDAORequest.getMessage());
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();  // Print the stack trace to the console
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
