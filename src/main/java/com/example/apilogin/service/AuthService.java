package com.example.apilogin.service;

import com.example.apilogin.domain.dto.LoginDTO;
import com.example.apilogin.domain.entity.User;
import com.example.apilogin.domain.redis.Token;
import com.example.apilogin.exception.IncorrectCredentialsException;
import com.example.apilogin.exception.NotAuthorizedException;
import com.example.apilogin.repository.EmailRepository;
import com.example.apilogin.repository.TokenRepository;
import com.example.apilogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final EmailRepository emailRepository;

    public Token authenticate(UUID token) throws NotAuthorizedException {
        if (!tokenRepository.existsByToken(token)) {
            log.error("Unauthorized token {}", token);
            throw new NotAuthorizedException("Invalid token");
        }
        return tokenRepository.findTokenByToken(token);
    }

    @Transactional
    public UUID login(LoginDTO login) throws NotAuthorizedException {
        if (!userRepository.existsByLogin(login.login()))
            throw new NotAuthorizedException("Unregistered user");
        if (!userRepository.existsByLoginAndPassword(login.login(), login.password()))
            throw new NotAuthorizedException("Password does not match");
        User user = userRepository.findUserByLogin(login.login());
        if (user.isLocked())
            throw new NotAuthorizedException("Your account is blocked, confirm your email to release it");
        log.info("Returning token for the user {}", user.getId());
        if (!tokenRepository.existsById(user.getId()))
            return tokenRepository.save(
                    Token.builder()
                            .id(user.getId())
                            .token(UUID.randomUUID())
                            .build()
            ).getToken();
        return tokenRepository.findTokenById(user.getId())
                .getToken();
    }

    @Transactional
    public void confirmation(Long id) throws NotAuthorizedException, IncorrectCredentialsException {
        if (!emailRepository.existsById(id))
            throw new NotAuthorizedException("Invalid confirmation");
        User user = userRepository.findUserById(id);
        if (!user.isLocked())
            throw new IncorrectCredentialsException("Already confirmed");
        userRepository.save(
                User.builder()
                        .id(id)
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .locked(false)
                        .build()
        );
        emailRepository.deleteById(id);
    }
}