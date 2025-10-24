package com.lab.jwtcore;

import com.lab.jwtcore.controller.RequestHandler;
import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.model.User;
import com.lab.jwtcore.service.AuthService;

public class Main {
    public static void main(String[] args) {
        AuthService auth = new AuthService();
        RequestHandler handler = new RequestHandler();
        User user = new User("user", "예린", "pass");

        // 1. 로그인 및 토큰 발급
        AuthTokens tokens = auth.login(user);
        if(tokens == null) return;

        // 2. Access Token 헤더 형식으로 전달
        String header = "Bearer " + tokens.getAccessToken();

        // 3. 요청 시뮬레이션
        System.out.println("\n[보호된 API 요청 시뮬레이션]");
        handler.handleRequest(header);

        // 4. 잘못된 토큰으로 테스트
        System.out.println("\n[잘못된 토큰 요청 시뮬레이션]");
        handler.handleRequest("Bearer invalid_token");

// -------- 이전 테스트 코드 -------
//        // 1. 로그인 및 토큰 발급
//        AuthTokens tokens = auth.login(user);
//
//        if(tokens == null) return;
//
//        System.out.println("Access Token: " + tokens.getAccessToken());
//        System.out.println("Refresh Token: " + tokens.getRefreshToken());
//
//        System.out.println("\n[Access Token 검증]");
//        auth.verifyToken(tokens.getAccessToken());
//
//        // 2. 만료 토큰 테스트
//        try{
//            System.out.println("3초 대기 중...");
//            Thread.sleep(3000);
//        } catch(InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("\n2차 검증 - 3초 후");
//        auth.verifyToken(tokens.getAccessToken());
//
//        // 3. Access Token 재발급 테스트
//        System.out.println("\n[Access Token 재발급 테스트]");
//        String newAccess = auth.refreshAccessToken(user.getId(), tokens.getRefreshToken());
//        System.out.println("새 Access Token: " + newAccess);
    }
}