package com.example.apilogin.domain.entity;

import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.time.LocalDate;

@RedisHash
@Builder
public class Verification {
    @Id
    @Indexed
    private Long id;
    private LocalDate date;
}
