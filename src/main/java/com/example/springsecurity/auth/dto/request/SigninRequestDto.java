package com.example.springsecurity.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninRequestDto {
    private String username;
    private String password;
}
