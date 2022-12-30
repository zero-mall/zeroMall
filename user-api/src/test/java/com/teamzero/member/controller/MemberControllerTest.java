package com.teamzero.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.domain.UserVo;
import com.teamzero.domain.util.Aes256Util;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @MockBean
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_PASSWORD = "123456";

    @Test
    void getMemberInfo() throws Exception {

        // given
        String token = Jwts.builder()
                .setClaims(Jwts.claims()
                        .setId(String.valueOf(1L))
                        .setSubject(Aes256Util.encrypt(TEST_EMAIL)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1000L * 60 * 60 * 6))
                .signWith(SignatureAlgorithm.HS256, "TEAMZERO_ZEROMALL")
                .compact();

        UserVo vo = new UserVo(1L, TEST_EMAIL, "BASIC");

        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .nickname("홍길동")
                .email(TEST_EMAIL)
                .password(BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()))
//                .memberGradeEntity(new MemberGradeEntity(1L, ,"BASIC", 2))
                .build();

        given(jwtAuthenticationProvider.getUserVo(anyString()))
                .willReturn(vo);

        given(memberService.findByMemberIdAndEmail(anyLong(), anyString()))
                .willReturn(member);

        // when & then
        mockMvc.perform(get("/member/info")
                .header("X-AUTH-TOKEN", token)
        )
                .andDo(print())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.nickname").value("홍길동"));

    }
}