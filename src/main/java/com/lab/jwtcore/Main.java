package com.lab.jwtcore;

import com.lab.jwtcore.util.JwtUtil;

public class Main {
    public static void main(String[] args) {
        String token = JwtUtil.generateToken("user01", "예린");
        System.out.println("Generated JWT Token: " + token);

        JwtUtil.validateToken(token);

        // 1. 토큰 변조 테스트
        System.out.println("Invalid Token Test: ");
        JwtUtil.validateToken(token + "x");// 토큰 변조

        // 2. 만료 토큰 테스트
        try{
            System.out.println("\n 3초 대기 중...");
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("2차 검증 - 3초 후");
        JwtUtil.validateToken(token);
    }
}