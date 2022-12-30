package com.teamzero.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamzero.domain.util.Aes256Util;
import com.teamzero.member.application.SignInApplication;
import com.teamzero.member.domain.model.dto.SignInDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignInController.class)
class SignInControllerTest {

    @MockBean
    SignInApplication signInApplication;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_PASSWORD = "123456";

    @Test
    void memberSignInToken() throws Exception {

        // given
        String token = Jwts.builder()
                .setClaims(Jwts.claims()
                        .setId(String.valueOf(1L))
                        .setSubject(Aes256Util.encrypt(TEST_EMAIL)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1000L * 60 * 60 * 6))
                .signWith(SignatureAlgorithm.HS256, "TEAMZERO_ZEROMALL")
                .compact();

        given(signInApplication.memberSignInToken(any()))
                .willReturn(token);

        // when & then
        mockMvc.perform(post("/signIn/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SignInDto(TEST_EMAIL, TEST_PASSWORD)
                        ))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }
}