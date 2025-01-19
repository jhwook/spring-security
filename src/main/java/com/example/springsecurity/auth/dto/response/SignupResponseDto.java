package com.example.springsecurity.auth.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupResponseDto {
    private String username;
    private String nickname;
    private List<AuthorityDto> authorities;

    @Data
    public static class AuthorityDto {
        private String authorityName;

        public AuthorityDto(String authorityName) {
            this.authorityName = authorityName;
        }
    }
}
