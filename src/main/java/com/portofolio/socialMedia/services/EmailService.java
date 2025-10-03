package com.portofolio.socialMedia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender jms;

    @Value("$(spring.mail.username)")
    private String sender;

    public void sendEmail(
            String recipient,
            String subject,
            String msgBody) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(sender);
            mail.setSubject(subject);
            mail.setTo(recipient);
            mail.setText(msgBody);
            jms.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatusCode.valueOf(452), "Email Failed to sent");
        }

    }
}