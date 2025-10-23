package com.lab.jwtcore.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // 1. 비밀키 하나 생성(HS256 알고리즘 사용)
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 3. 토큰 생성 메서드
    // Access Token
    public static String generateToken(String userId, String userName) {
        long now = System.currentTimeMillis();
//         long expireTime = 1000 * 60 * 10; // 10분
        long expireTime = 1000 * 3; // 3초

        return Jwts.builder()
                   .setSubject(userId) // 토큰 주제(보통 userId)
                   .claim("name",userName) // 사용자 이름
                   .setIssuedAt(new Date(now)) // 발급 시간
                   .setExpiration(new Date(now + expireTime)) // 만료 시간
                   .signWith(key) // 비밀키로 서명
                   .compact(); // JWT 문자열 생성
    }

    // Refresh Token
    public static String generateRefreshToken(String userId) {
        long now = System.currentTimeMillis();
        long expireTime = 1000 * 60 * 60 * 24 *7; // 7일
        return Jwts.builder()
                   .setSubject(userId) // 토큰 주제(보통 userId)
                   .setIssuedAt(new Date(now)) // 발급 시간
                   .setExpiration(new Date(now + expireTime)) // 만료 시간
                   .signWith(key) // 비밀키로 서명
                   .compact(); // JWT 문자열 생성
    }

    // 4. JWT 검증 메서드
    public static void validateToken(String token) {
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                                     .setSigningKey(key) // 생성시 사용한 키로 검증
                                     .build()
                                     .parseClaimsJws(token); // 토큰 구조를 해석해서 Payload 추출


            System.out.println("Token is valid.");
            System.out.println("User ID: " + claims.getBody().getSubject());
            System.out.println("User Name: " + claims.getBody().get("name"));
            System.out.println("Expiration: " + claims.getBody().getExpiration());

        } catch(JwtException e) { // 유효하지 않은 토큰일 경우 발생 (만료, 위조 등)
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
    }

    // 2. 키를 확인하기 위한 메서드(테스트용)
    public static void printKeyInfo() {
        System.out.println("Secret key algorithm: " + key.getAlgorithm());
    }
}