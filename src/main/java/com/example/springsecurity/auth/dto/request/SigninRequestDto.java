package com.example.springsecurity.auth.dto.request;

import lombok.Getter;

@Getter
public class SigninRequestDto {
    private String username;
    private String password;
}
