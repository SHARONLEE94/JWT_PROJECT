package com.lab.jwtcore;

import com.lab.jwtcore.service.AuthService;
import com.lab.jwtcore.util.JwtUtil;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        // 로그인 -> 토큰 발급
        String token = authService.login("user", "pass");

        if(token != null) {
            System.out.println("발급된 토큰: " + token);
            authService.verifyToken(token);

            // 2. 만료 토큰 테스트
            try{
                System.out.println("3초 대기 중...");
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("2차 검증 - 3초 후");
            authService.verifyToken(token);
        }
    }
}