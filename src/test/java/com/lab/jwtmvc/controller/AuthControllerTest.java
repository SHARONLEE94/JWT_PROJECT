package com.lab.jwtmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.jwtcore.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User("user", "예린","pass");
    }

    @Test
    @DisplayName("로그인 성공 시 AccessToken과 RefreshToken이 반환되어야 한다.")
    void loginSuccessTest() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("accessToken");
        assertThat(responseBody).contains("refreshToken");
    }

    @Test
    @DisplayName("AccessToken이 유효하면 secure API 접근 가능해야 한다")
    void secureAccessTest() throws Exception {
        // 로그인 먼저 수행 → 토큰 획득
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // accessToken만 추출
        String accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();

        // secure API 호출
        mockMvc.perform(get("/api/auth/secure")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("접근 허용됨(Protected Resource)"));
    }
}
