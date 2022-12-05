package com.example.apilogin.service;

import com.example.apilogin.domain.entity.User;
import com.example.apilogin.domain.entity.Verification;
import com.example.apilogin.domain.model.Email;
import com.example.apilogin.exception.IncorrectCredentialsException;
import com.example.apilogin.exception.NotAuthorizedException;
import com.example.apilogin.repository.EmailRepository;
import com.example.apilogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.UUID;

import static java.time.LocalTime.now;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmService {
    final EmailSenderService emailSenderService;
    final EmailRepository emailRepository;
    final UserRepository userRepository;

    @Transactional
    public void confirmation(UUID token) throws NotAuthorizedException, IncorrectCredentialsException, MessagingException {
        if (!emailRepository.existsByVerify(token))
            throw new NotAuthorizedException("Invalid confirmation");
        Verification verification = emailRepository.findByVerify(token);
        User user = userRepository.findUserById(verification.getId());
        if (verification.getExpiredDate().isBefore(now())) {
            Verification newVerification = emailRepository.save(
                Verification.builder()
                        .id(user.getId())
                        .verify(UUID.randomUUID())
                        .expiredDate(now().plusMinutes(15))
                        .build()
            );
            emailSenderService.sendConfirmation(
                    Email.builder()
                            .destiny(user.getLogin())
                            .subject("Welcome to our system " + user.getName())
                            .content(emailSenderService.emailModel(newVerification.getVerify()))
                            .build()
            );
            throw new NotAuthorizedException("Link expired, re-sending a new link to your email");
        }
        if (!user.isLocked())
            throw new IncorrectCredentialsException("Already confirmed");
        userRepository.save(
                User.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .locked(false)
                        .build()
        );
        emailRepository.deleteById(verification.getId());
    }
}
