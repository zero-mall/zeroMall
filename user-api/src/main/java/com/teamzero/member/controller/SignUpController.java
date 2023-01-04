package com.teamzero.member.controller;

import com.teamzero.member.application.SignUpApplication;
import com.teamzero.member.domain.model.dto.AdminInfoDto;
import com.teamzero.member.domain.model.dto.SignUpDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
@Api(tags = {"SignUpController"}, description = "일반회원/관리자 회원 가입 API")
public class SignUpController {

    private final SignUpApplication signUpApplication;

    /**
     * 일반 회원 가입
     */
    @PostMapping("/member")
    @ApiOperation(value = "이메일 회원 가입")
    public ResponseEntity<SignUpDto.Response> registerMember(
        @RequestBody SignUpDto.Request signUp) {
        return ResponseEntity.ok(signUpApplication.register(signUp));
    }

    /**
     * 이메일 중복 체크
     * 에러 정책 확립 후 return값 변경
     */
    @GetMapping("/checkMail")
    @ApiOperation(value = "이메일 중복 체크")
    public ResponseEntity<Boolean> checkEmailDuplicate(
        @RequestParam String email) {
        return ResponseEntity.ok(signUpApplication.exsitsByEmail(email));
    }

    /**
     * 회원가입 후 이메일 인증
     */
    @GetMapping("/email-auth")
    @ApiOperation(value = "이메일 인증")
    public ResponseEntity<SignUpDto.Response> sendMemberEmailAuth(
        @RequestParam String key, @RequestParam String id) {
        return ResponseEntity.ok(
            signUpApplication.memberEmailAuthCheck(id, key));
    }

    /**
     * 관리자 회원 가입
     */
    @PostMapping("/admin")
    @ApiOperation(value = "관리자 회원 가입")
    public ResponseEntity<AdminInfoDto> registerAdmin(
        @RequestBody SignUpDto.Request signUp) {
        return ResponseEntity.ok(signUpApplication.registerAdmin(signUp));
    }

}
