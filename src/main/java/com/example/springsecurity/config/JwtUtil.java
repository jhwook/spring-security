package com.example.springsecurity.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.rmi.ServerException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 1시간
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    public String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String username) {
        return createToken(userId, username, ACCESS_TOKEN_TIME);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        return createToken(userId, null, REFRESH_TOKEN_TIME);
    }

    // 공통 토큰 생성 로직
    public String createToken(Long userId, String username, long expireTime) {
        Date now = new Date();

        var builder = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(now.getTime() + expireTime))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm);

        if (username != null) {
            builder.claim("username", username);
        }

        return BEARER_PREFIX + builder.compact();
    }

    public String substringToken(String tokenValue) throws ServerException {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        throw new ServerException("Not Found Token");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}