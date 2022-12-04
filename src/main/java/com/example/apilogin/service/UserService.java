package com.example.apilogin.service;

import com.example.apilogin.domain.dto.UserDTO;
import com.example.apilogin.domain.entity.User;
import com.example.apilogin.domain.model.Email;
import com.example.apilogin.exception.IncorrectCredentialsException;
import com.example.apilogin.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final EmailSenderService emailSenderService;

    @Transactional
    public void register(UserDTO userDTO) throws IncorrectCredentialsException {
        if (userRepository.existsByLogin(userDTO.login())) {
            log.warn("Registration attempt failed");
            throw new IncorrectCredentialsException("User has already been registered");
        }
        log.info("Registering new user {}", userDTO);
        User user = userRepository.save(
                User.builder()
                        .login(userDTO.login())
                        .password(userDTO.password())
                        .build()
        );
        emailSenderService.sendEmail(
                Email.builder()
                        .destiny(user.getLogin())
                        .subject("Welcome to our system")
                        .content("The registration was carried out successfully")
                        .build()
        );
    }

    @Transactional
    public void update(UserDTO userDTO, Long id) throws IncorrectCredentialsException {
        if (!userRepository.existsById(id))
            throw new IncorrectCredentialsException("Usuário não cadastrado");
        log.info("Updating user {}", userDTO);
        User user = userRepository.findUserById(id);
        userRepository.save(
                User.builder()
                        .id(id)
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .build()
        );
        emailSenderService.sendEmail(
                Email.builder()
                        .destiny(user.getLogin())
                        .subject("Update on your account")
                        .content("Update performed successfully")
                        .build()
        );
    }

    @Async
    @Transactional
    public void delete(Long id) throws IncorrectCredentialsException {
        if (!userRepository.existsById(id))
            throw new IncorrectCredentialsException("Unregistered user");
        log.info("Deleting user {}", id);
        User user = userRepository.findUserById(id);
        userRepository.delete(user);
        emailSenderService.sendEmail(
                Email.builder()
                        .destiny(user.getLogin())
                        .subject("Your account has been deleted")
                        .content("Your account has been successfully deleted")
                        .build()
        );
    }

    public User toView(Long id) throws IncorrectCredentialsException {
        if (!userRepository.existsById(id))
            throw new IncorrectCredentialsException("Usuário não cadastrado");
        log.info("Returning user {}", id);
        return userRepository.findUserById(id);
    }
}
