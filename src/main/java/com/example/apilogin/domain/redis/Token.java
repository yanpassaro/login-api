package com.example.apilogin.domain.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Data
@Builder
@RedisHash
public class Token {
    @Id
    @Indexed
    private Long id;
    @Indexed
    private UUID token;
}
