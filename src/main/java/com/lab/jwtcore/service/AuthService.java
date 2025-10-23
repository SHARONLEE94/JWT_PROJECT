package com.lab.jwtcore.service;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.RefreshTokenRecord;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.store.RefreshStore;
import com.lab.jwtcore.util.JwtUtil;

public class AuthService {

    // 싱글톤 인스턴스 주입
    private final RefreshStore refreshStore = RefreshStore.getInstance();

    // 로그인 메서드(DB가 없으니 임시 사용자)
    // 로그인 시 Access + Refresh Token 동시 발급
    public AuthTokens login(User user) {

        if("user".equals(user.getId()) && "pass".equals(user.getPassword())) {
            System.out.println("로그인 성공 (" + user.getName() + ")");

            String accessToken = JwtUtil.generateToken(user.getId(), user.getName());
            String refreshToken  = JwtUtil.generateRefreshToken (user.getId());

            // 리프레시 토큰 저장
            refreshStore.save(new RefreshTokenRecord(user.getId(), refreshToken));

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

    // Refresh Token 검증 후 Access Token 재발급
    public String refreshAccessToken(String userId, String refreshToken) {
        RefreshTokenRecord record = refreshStore.findByUserId(userId);

        if(record == null || !record.equalsTo(refreshToken)) {
            System.out.println("유효하지 않은 Refresh Token");
            return null;
        }

        try{
            JwtUtil.validateToken(refreshToken);
            String newAccess = JwtUtil.generateToken(userId, "예린");
            System.out.println("새로운 Access Token 발급");
            return newAccess;
        } catch(Exception e) {
            System.out.println("Refresh Token 만료 - 다시 로그인 필요");
            return null;
        }
    }
}