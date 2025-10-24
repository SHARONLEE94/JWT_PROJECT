package com.lab.jwtcore.controller;

import com.lab.jwtcore.util.JwtUtil;

public class RequestHandler {
    public void handleRequest(String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Unauthorized – JWT 토큰이 없습니다.");
            return;
        }

        String token = authorizationHeader.substring(7); // "Bearer "제거

        try{
            JwtUtil.validateToken(token);
            System.out.println("Access Token 유효 – 요청 허용됨 (Protected Resource 접근)");
        } catch(Exception e) {
            System.out.println("Access Token 검증 실패 – " + e.getMessage());
        }
    }

}
