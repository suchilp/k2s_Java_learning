package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok("USER_REGISTERED");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok("JWT_TOKEN_PLACEHOLDER");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok("OTP_VERIFIED");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok("JWT_TOKEN_REFRESHED");
    }

    static class RegisterRequest {
        public String username;
        public String password;
        public String email;
        public String mobile;
    }

    static class LoginRequest {
        public String username;
        public String password;
    }

    static class OtpRequest {
        public String username;
        public String otpCode;
    }

    static class RefreshTokenRequest {
        public String refreshToken;
    }
}
