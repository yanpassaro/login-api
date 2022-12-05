package com.example.apilogin.service;

import com.example.apilogin.domain.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(Email email) {
        log.info("Sending email to {}", email.getDestiny());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getDestiny());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        mailSender.send(message);
    }

    @Async
    public void sendConfirmation(Email email) throws MessagingException {
        log.info("Sending confirm to {}", email.getDestiny());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email.getDestiny());
        helper.setSubject(email.getSubject());
        helper.setText(email.getContent(), true);
        mailSender.send(message);
    }

    public String emailModel(UUID token) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Welcome</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div style=\"box-shadow:1px 1px 1px 1px rgba(0.2,0.2,0.2,0.2);padding: 2px 16px 2px 16px; transition: 0.3s; border-radius: 2px;\">\n" +
                "    <div style=\"margin: auto;padding:40px 20px 20px 40px;\">\n" +
                "      <h3 style=\"font-size: 30px; font-family: sans-serif; color: black\">Confirm your email</h3>\n" +
                "      <p style=\"font-size: 20px; font-family: sans-serif; color: black\">\n" +
                "        Thank you for registering. Please click on the below link to activate your account\n" +
                "      </p>\n" +
                "      <p style=\"margin-bottom: 15px;border-left: 6px solid #4682B4;padding: 4px 12px;background-color:azure; font-size: 16px; font-family: serif; color: black\">\n" +
                "        Link will expire in 15 minutes.\n" +
                "      </p>\n" +
                "      <hr style=\"border-top: 1px solid rgba(240,255,255,0.71);\">\n" +
                "      <button type=\"button\" style=\"background: #535b62; color: #ffffff; border: 2px solid #535b62; border-radius: 3px;padding: 8px 7px;text-align: center;text-decoration: none;display: inline-block;font-size: 13px;\">\n" +
                "        <a href=\"http://localhost:8080/api/v1/user/confirm?token="+ token +"\" style=\"color: #ffffff; text-decoration-color: #535b62;font-family: sans-serif\">Active now</a>\n" +
                "      </button>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
