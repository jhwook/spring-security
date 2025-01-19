package com.example.springsecurity.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
}
