package com.lab.jwtmvc.controller;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.service.AuthService;
import com.lab.jwtcore.util.JwtUtil;
import com.lab.jwtmvc.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService = new AuthService();

    // 로그인 → Access + Refresh Token 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        AuthTokens tokens = authService.login(user);
        return ResponseEntity.ok(ApiResponse.success(tokens));
    }

    // Refresh Token으로 Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthTokens tokens) {
        String newAccess = authService.refreshAccessToken("user", tokens.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(newAccess));
    }

    // 보호된 리소스 접근 테스트
    @GetMapping("/secure")
    public ResponseEntity<?> secure(@RequestHeader("Authorization") String header) {
        String token = header.substring(7); // Bearer 제거
        JwtUtil.validateToken(token);
        return ResponseEntity.ok("접근 허용됨(Protected Resource)");
    }
}
