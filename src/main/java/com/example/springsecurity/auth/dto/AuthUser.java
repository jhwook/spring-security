package com.example.springsecurity.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthUser {

    private final Long id;
    private final String username;

    public static AuthUser from(Long id, String username) {
        return new AuthUser(id, username);
    }
}