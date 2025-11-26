package com.lab.jwtmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lab.jwtmvc", "com.lab.jwtcore"})
public class JwtMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtMvcApplication.class, args);
        System.out.println("JWT MVC Application Started");
    }
}
