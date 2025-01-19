package com.example.springsecurity.auth.service;

import com.example.springsecurity.auth.dto.request.SigninRequestDto;
import com.example.springsecurity.auth.dto.request.SignupRequestDto;
import com.example.springsecurity.auth.dto.response.SigninResponseDto;
import com.example.springsecurity.auth.dto.response.SignupResponseDto;
import com.example.springsecurity.auth.entity.Authority;
import com.example.springsecurity.auth.entity.RefreshToken;
import com.example.springsecurity.auth.entity.User;
import com.example.springsecurity.auth.repository.AuthRepository;
import com.example.springsecurity.auth.repository.AuthorityRepository;
import com.example.springsecurity.auth.repository.RefreshTokenRepository;
import com.example.springsecurity.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        Optional<User> checkUsername = authRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<User> checkNickname = authRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 nickname입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(username, nickname, encodedPassword);
        authRepository.save(user);

        Authority authority = new Authority("ROLE_USER", user);
        authorityRepository.save(authority);

        SignupResponseDto response = new SignupResponseDto();
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());

        SignupResponseDto.AuthorityDto authorityDto = new SignupResponseDto.AuthorityDto(authority.getAuthorityName());
        response.setAuthorities(List.of(authorityDto));

        return response;
    }

    @Transactional
    public SigninResponseDto signin(SigninRequestDto requestDto) {
        User user = authRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = generateTokens(
                user.getId(),
                user.getUsername()
        );

        SigninResponseDto response = new SigninResponseDto();
        response.setToken(token);

        return response;
    }

    @Transactional
    public String generateTokens(Long userId, String username) {
        String accessToken = jwtUtil.createAccessToken(userId, username);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        saveRefreshToken(userId, refreshToken);
        return accessToken;
    }

    private void saveRefreshToken(Long userId, String refreshToken) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);

        if (existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            token.setToken(refreshToken);
            token.setExpiration(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);
        } else {
            RefreshToken newToken = new RefreshToken();
            newToken.setUserId(userId);
            newToken.setToken(refreshToken);
            newToken.setExpiration(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);
            refreshTokenRepository.save(newToken);
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        String newAccessToken = jwtUtil.createAccessToken(userId, claims.get("username", String.class));

        return newAccessToken;
    }
}
