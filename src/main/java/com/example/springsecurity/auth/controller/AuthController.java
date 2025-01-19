package com.example.springsecurity.auth.controller;

import com.example.springsecurity.auth.dto.request.SigninRequestDto;
import com.example.springsecurity.auth.dto.request.SignupRequestDto;
import com.example.springsecurity.auth.dto.response.SigninResponseDto;
import com.example.springsecurity.auth.dto.response.SignupResponseDto;
import com.example.springsecurity.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "Registers a new user.")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/sign")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody SigninRequestDto requestDto) {
        return ResponseEntity.ok(authService.signin(requestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(@RequestBody String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }
}
