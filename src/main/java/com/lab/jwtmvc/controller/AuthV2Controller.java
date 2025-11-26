package com.lab.jwtmvc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthV2Controller {

    @PostMapping("/login")
    public String login() {
        return "JWT v2 login success";
    }

}
