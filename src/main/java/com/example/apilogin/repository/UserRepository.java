package com.example.apilogin.repository;

import com.example.apilogin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    boolean existsByLoginAndPassword(String email, String password);

    User findUserByLogin(String login);

    User findUserById(Long id);
}