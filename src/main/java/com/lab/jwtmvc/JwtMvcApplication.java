package com.lab.jwtmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtMvcApplication.class, args);
        System.out.println("JWT MVC Application Started");
    }
}
