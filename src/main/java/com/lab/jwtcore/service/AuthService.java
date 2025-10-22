package com.lab.jwtcore.service;

import com.lab.jwtcore.model.User;
import com.lab.jwtcore.util.JwtUtil;

public class AuthService {

    // 로그인 메서드(DB가 없으니 임시 사용자)
    public String login(User user) {
        if("user".equals(user.getId()) && "pass".equals(user.getPassword())) {
            System.out.println("로그인 성공 (" + user.getName() + ")");
            return JwtUtil.generateToken(user.getId(), user.getName());
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