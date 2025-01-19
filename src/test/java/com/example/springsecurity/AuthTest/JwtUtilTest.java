package com.example.springsecurity.AuthTest;

import com.example.springsecurity.config.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.ServerException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.secretKey = Base64.getEncoder().encodeToString("mySecretKey123456789012345678901234567890".getBytes());
        jwtUtil.init();
    }

    @Test
    void testCreateAccessToken() {
        String token = jwtUtil.createAccessToken(1L, "testUser");
        assertNotNull(token, "Access Token should not be null");
        assertTrue(token.startsWith("Bearer "), "Token should start with 'Bearer '");
    }

    @Test
    void testCreateRefreshToken() {
        String token = jwtUtil.createRefreshToken(1L);
        assertNotNull(token, "Refresh Token should not be null");
        assertTrue(token.startsWith("Bearer "), "Token should start with 'Bearer '");
    }

    @Test
    void testExtractClaims() {
        String token = jwtUtil.createAccessToken(1L, "testUser");

        String tokenWithoutPrefix = token.substring("Bearer ".length());
        Claims claims = jwtUtil.extractClaims(tokenWithoutPrefix);

        assertEquals("1", claims.getSubject(), "The subject (userId) should match");
        assertEquals("testUser", claims.get("username"), "The username should match");
    }

    @Test
    void testValidToken() {
        String token = jwtUtil.createAccessToken(1L, "testUser");

        String tokenWithoutPrefix = token.substring("Bearer ".length());
        assertTrue(jwtUtil.isTokenValid(tokenWithoutPrefix), "The token should be valid");
    }

    @Test
    void testInvalidTokenSignature() {
        String validToken = jwtUtil.createAccessToken(1L, "testUser");

        String[] tokenParts = validToken.split("\\.");
        String invalidToken = tokenParts[0] + "." + tokenParts[1] + ".invalidSignature";

        assertThrows(SignatureException.class, () -> {
            jwtUtil.extractClaims(invalidToken.substring("Bearer ".length()));
        }, "Invalid signature should throw SignatureException");
    }

    @Test
    void testExpiredToken() {
        jwtUtil = new JwtUtil();
        jwtUtil.secretKey = Base64.getEncoder().encodeToString("mySecretKey123456789012345678901234567890".getBytes());
        jwtUtil.init();

        String token = jwtUtil.createToken(1L, "testUser", 1);

        String tokenWithoutPrefix = token.substring("Bearer ".length());

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractClaims(tokenWithoutPrefix),
                "Expired token should throw ExpiredJwtException");
    }

    @Test
    void testSubstringToken() throws ServerException {
        String token = "Bearer myJwtToken";
        String result = jwtUtil.substringToken(token);
        assertEquals("myJwtToken", result, "The token should remove the 'Bearer ' prefix");
    }

    @Test
    void testSubstringTokenThrowsException() {
        String token = "InvalidToken";
        assertThrows(ServerException.class, () -> jwtUtil.substringToken(token),
                "Invalid token without 'Bearer ' prefix should throw ServerException");
    }
}