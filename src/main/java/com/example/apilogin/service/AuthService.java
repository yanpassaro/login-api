package com.example.apilogin.service;

import com.example.apilogin.domain.dto.LoginDTO;
import com.example.apilogin.domain.entity.User;
import com.example.apilogin.domain.redis.Token;
import com.example.apilogin.exception.NotAuthorizedException;
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
        log.info("Returning token for the user {}", user.getId());
        if (!tokenRepository.existsById(user.getId()))
            return tokenRepository.save(
                    Token.builder()
                            .id(user.getId())
                            .token(UUID.randomUUID())
                            .build()
            ).getToken();
        return tokenRepository
                .findTokenById(user.getId())
                .getToken();
    }
}