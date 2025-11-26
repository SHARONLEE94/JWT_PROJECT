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
    private ObjectMapper objectMapper; // JSON 직렬화용

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User("user", "예린","pass");
    }

    @Test
    @DisplayName("로그인 성공 시 AccessToken/RefreshToken이 반환되어야 한다.")
    void loginSuccessTest() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(testUser)))
                                  .andExpect(status().isOk())
                                  .andExpect(jsonPath("$.success").value(true))
                                  .andExpect(jsonPath("$.data.accessToken").exists())
                                  .andExpect(jsonPath("$.data.refreshToken").exists())
                                  .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Login Response: " + responseBody);
    }

    @Test
    @DisplayName("AccessToken이 유효하면 secure API 접근 가능해야 한다")
    void secureAccessTest() throws Exception {
        // 로그인 → 토큰 발급
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(testUser)))
                                      .andExpect(status().isOk())
                                      .andReturn()
                                      .getResponse()
                                      .getContentAsString();

        // accessToken만 추출
        String accessToken = objectMapper.readTree(loginResponse)
                                         .path("data")
                                         .path("accessToken")
                                         .asText();

        // secure API 호출
        mockMvc.perform(get("/api/auth/secure")
               .header("Authorization", "Bearer " + accessToken))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data").value("접근 허용됨(Protected Resource)"));
    }

    @Test
    @DisplayName("로그인 실패 시 401 응답과 ApiResponse.fail 구조가 반환되어야 한다.")
    void loginFailTest() throws Exception {
        User invalidUser = new User("invalidUser", "Invalid", "wrongPass");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(invalidUser)))
                                  .andExpect(status().isUnauthorized())
                                  .andExpect(jsonPath("$.success").value(false))
                                  .andExpect(jsonPath("$.message").exists())
                                  .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Login Fail Response: " + responseBody);
    }
}
