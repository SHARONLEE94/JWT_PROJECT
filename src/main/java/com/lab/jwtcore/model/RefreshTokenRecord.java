package com.lab.jwtcore.model;

public class RefreshTokenRecord {
    private String userId;
    private String refreshToken;

    public RefreshTokenRecord(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean equalsTo(String token) {
        return this.refreshToken.equals(token);
    }

}
