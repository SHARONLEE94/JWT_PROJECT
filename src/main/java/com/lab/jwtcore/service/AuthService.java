package com.lab.jwtcore.service;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.util.JwtUtil;

public class AuthService {

    // 로그인 메서드(DB가 없으니 임시 사용자)
    // 로그인 시 Access + Refresh Token 동시 발급
    public AuthTokens login(User user) {

        if("user".equals(user.getId()) && "pass".equals(user.getPassword())) {
            System.out.println("로그인 성공 (" + user.getName() + ")");

            String accessToken = JwtUtil.generateToken(user.getId(), user.getName());
            String refreshToken  = JwtUtil.generateRefreshToken (user.getId());

            return new AuthTokens(accessToken, refreshToken);
        } else {
            System.out.println("로그인 실패");
            return null;
        }
    }

    // 토큰 검증 메서드
    public void verifyToken(String token) {
        JwtUtil.validateToken(token);
    }
}