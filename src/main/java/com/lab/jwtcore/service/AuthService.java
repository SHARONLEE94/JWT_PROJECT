package com.lab.jwtcore.service;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.RefreshTokenRecord;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.store.RefreshStore;
import com.lab.jwtcore.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthService {

    // 싱글톤 인스턴스 주입
    private final RefreshStore refreshStore = RefreshStore.getInstance();

    // 로그인 메서드(DB가 없으니 임시 사용자)
    // 로그인 시 Access + Refresh Token 동시 발급
    public AuthTokens login(User user) {

        if("user".equals(user.getId()) && "pass".equals(user.getPassword())) {
            log.info("로그인 성공: {}", user.getName());

            String accessToken = JwtUtil.generateToken(user.getId(), user.getName());
            String refreshToken  = JwtUtil.generateRefreshToken (user.getId());

            // 리프레시 토큰 저장
            refreshStore.save(new RefreshTokenRecord(user.getId(), refreshToken));
            return new AuthTokens(accessToken, refreshToken);
        } else {
            log.warn("로그인 실패 - id: {}", user.getId());
            throw new JwtException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    // 토큰 검증 메서드
    public void verifyToken(String token) {
        JwtUtil.validateToken(token); // 유효하지 않으면 예외 발생
    }

    // Refresh Token 검증 후 Access Token 재발급
    public String refreshAccessToken(String userId, String refreshToken) {
        RefreshTokenRecord record = refreshStore.findByUserId(userId);

        if(record == null || !record.equalsTo(refreshToken)) {
            log.warn("리프레시 토큰 불일치 (userId: {})", userId);
            throw new JwtException("유효하지 않은 Refresh Token");
        }

        try{
            JwtUtil.validateToken(refreshToken);
            String newAccess = JwtUtil.generateToken(userId, "예린");
            log.info("새로운 Access Token 발급 (userId={})", userId);
            return newAccess;
        } catch(Exception e) {
            log.warn("Refresh Token 만료 (userId: {})", userId);
            throw new JwtException("Refresh Token이 만료되었습니다. 다시 로그인하세요.");
        }
    }
}