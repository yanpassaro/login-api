package com.example.apilogin.repository;

import com.example.apilogin.domain.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Verification, Long> {
    boolean existsByVerify(UUID token);

    Verification findByVerify(UUID token);
}