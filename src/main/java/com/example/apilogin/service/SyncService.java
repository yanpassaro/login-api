package com.example.apilogin.service;

import com.example.apilogin.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class SyncService {
    final TokenRepository tokenRepository;

    @Transactional
    @Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.DAYS)
    public void flushAllEmpresas(){
        log.info("Deleting all cached tokens");
        tokenRepository.deleteAll();
    }
}