package com.example.springsecurity.auth.repository;

import com.example.springsecurity.auth.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
