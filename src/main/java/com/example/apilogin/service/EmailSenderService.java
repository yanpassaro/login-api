package com.example.apilogin.service;

import com.example.apilogin.domain.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    final JavaMailSender mailSender;

    public void sendEmail(Email email) {
        log.info("Sending email to {}", email.getDestiny());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getDestiny());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        mailSender.send(message);
    }
}
