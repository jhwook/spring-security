package com.example.springsecurity.auth.repository;

import com.example.springsecurity.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);
}
