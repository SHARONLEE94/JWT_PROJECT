package com.lab.jwtmvc.controller;

import com.lab.jwtcore.model.AuthTokens;
import com.lab.jwtcore.service.AuthService;
import com.lab.jwtmvc.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthV2Controller {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthTokens login(@RequestBody LoginRequest request) {

        System.out.println(request.getUserId() + ", " + request.getUserName());

        String accessToken = authService.generateAccessToken(
                request.getUserId(),
                request.getUserName()
        );

        String refreshToken = authService.generateRefreshToken(
                request.getUserId()
        );

        authService.storeRefreshToken(request.getUserId(), refreshToken);

        return new AuthTokens(accessToken, refreshToken);
    }

}
