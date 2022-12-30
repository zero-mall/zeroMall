package com.teamzero.member.controller;

import com.teamzero.member.application.SignInApplication;
import com.teamzero.member.domain.model.dto.SignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signIn")
public class SignInController {

    private final SignInApplication signInApplication;

    /**
     * 일반회원 로그인 인증
     * - JWT 토큰 발행
     */
    @PostMapping("/member")
    public ResponseEntity<String> memberSignIn(@RequestBody SignInDto signIn){

        return ResponseEntity.ok(signInApplication.memberSignInToken(signIn));

    }

    /**
     * 관리자 로그인 인증
     * - JWT 토큰 발행
     */
    @PostMapping("/admin")
    public ResponseEntity<String> adminSignIn(@RequestBody SignInDto signIn){

        return ResponseEntity.ok(signInApplication.adminSignInToken(signIn));

    }

}
