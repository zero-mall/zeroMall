package com.teamzero.member.controller;

import com.teamzero.member.domain.model.dto.SignUp;
import com.teamzero.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {

    private final MemberService memberService;

    /**
     * 일반 회원 가입
     */
    @PostMapping("/member")
    public ResponseEntity<?> memberRegister(@RequestBody SignUp signUp) {
        var member = memberService.memberRegister(signUp);

        return ResponseEntity.ok(member);
    }

    /**
     * 이메일 중복 체크
     * 에러 정책 확립 후 return값 변경
     * @param email
     */
    @GetMapping("/checkMail")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email) {
        var result = memberService.findByEmail(email);
        return ResponseEntity.ok(result);
    }

    /**
     * 회원가입 후 이메일 인증
     */
    @GetMapping("/email-auth")
    public ResponseEntity<?> memberEmailAuth(@RequestParam String key, @RequestParam String id) {
        var result = memberService.memberEmailAuthCheck(id, key);

        return ResponseEntity.ok(result);
    }

    /**
     * 관리자 회원 가입
     */
    @PostMapping("/admin")
    public ResponseEntity<?> adminRegister(@RequestBody SignUp signUp) {
        //TODO 찬혁님, 여기에 작업 주시면 됩니다.
        return null;
    }

}
