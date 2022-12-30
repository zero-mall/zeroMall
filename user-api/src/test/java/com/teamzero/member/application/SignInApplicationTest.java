package com.teamzero.member.application;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.util.Aes256Util;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.dto.SignInDto;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignInApplicationTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @InjectMocks
    private SignInApplication signInApplication;

    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_PASSWORD = "123456";

    @Test
    @DisplayName("memberSingInFail : 아이디가 없는 경우")
    void memberSignInFail_memberNotFound() {

        // given
        given(memberRepository.findAllByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        TeamZeroException exception = assertThrows(TeamZeroException.class,
                () -> signInApplication.memberSignInToken(new SignInDto(TEST_EMAIL, TEST_PASSWORD)));

        // then
        Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("memberSingInFail : 비밀번호가 일치하지 않는 경우")
    void memberSignInFail_memberPasswordUnMatch() {

        // given
        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .nickname("홍길동")
                .email(TEST_EMAIL)
                .password(BCrypt.hashpw("5678912", BCrypt.gensalt()))
                .build();

        given(memberRepository.findAllByEmail(anyString()))
                .willReturn(Optional.of(member));

        // when
        TeamZeroException exception = assertThrows(TeamZeroException.class,
                () -> signInApplication.memberSignInToken(new SignInDto(TEST_EMAIL, TEST_PASSWORD)));

        // then
        Assertions.assertEquals(ErrorCode.MEMBER_SIGNIN_NOT_POSSIBLE, exception.getErrorCode());

    }

    @Test
    @DisplayName("memberSignInToken : 로그인 인증 토큰 발생 성공")
    void memberSignInToken() {

        // given
        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .nickname("홍길동")
                .email(TEST_EMAIL)
                .password(BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()))
                .build();

        String token = Jwts.builder()
                .setClaims(Jwts.claims()
                        .setId(String.valueOf(1L))
                        .setSubject(Aes256Util.encrypt(TEST_EMAIL)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1000L * 60 * 60 * 6))
                .signWith(SignatureAlgorithm.HS256, "TEAMZERO_ZEROMALL")
                .compact();

        given(memberRepository.findAllByEmail(anyString()))
                .willReturn(Optional.of(member));

        given(jwtAuthenticationProvider.createToken(anyLong(), anyString(), anyString()))
                .willReturn(token);

        // when
        String result = signInApplication.memberSignInToken(new SignInDto(TEST_EMAIL, TEST_PASSWORD));

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(token, result);

    }
}