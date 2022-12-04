package com.example.apilogin.repository;

import com.example.apilogin.domain.redis.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TokenRepository extends CrudRepository<Token, Long> {
    boolean existsByToken(UUID token);

    Token findTokenByToken(UUID token);

    Token findTokenById(Long id);

}