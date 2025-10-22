package com.lab.jwtcore.service;

import com.lab.jwtcore.util.JwtUtil;

public class AuthService {

    // 로그인 메서드(DB가 없으니 임시 사용자)
    public String login(String id, String password) {
        if("user".equals(id) && "pass".equals(password)) {
            System.out.println("로그인 성공");
            return JwtUtil.generateToken(id, "이예린");
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