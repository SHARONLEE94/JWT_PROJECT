package com.lab.jwtcore;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.service.AuthService;
import com.lab.jwtcore.util.JwtUtil;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        User user = new User("user", "예린", "pass");

        // 1. 로그인 및 토큰 발급
        AuthTokens tokens = authService.login(user);

        if(tokens != null) {
            System.out.println("Access Token: " + tokens.getAccessToken());
            System.out.println("Refresh Token: " + tokens.getRefreshToken());
            authService.verifyToken(tokens.getAccessToken());

            // 2. 만료 토큰 테스트
            try{
                System.out.println("3초 대기 중...");
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("2차 검증 - 3초 후");
            authService.verifyToken(tokens.getAccessToken());
        }
    }
}