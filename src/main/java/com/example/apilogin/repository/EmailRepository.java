package com.example.apilogin.repository;

import com.example.apilogin.domain.entity.Verification;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Verification, Long> {
}