package com.teamzero.member.controller;

import com.teamzero.member.domain.model.SignUp;
import com.teamzero.member.service.MemberService;
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
@RequestMapping("/member")
public class MeberController {

  private final MemberService memberService;

  /**
   * 이메일 중복 체크
   * 에러 정책 확립 후 return값 변경
   * @param email
   * @return
   */
  @GetMapping("/checkMail")
  public ResponseEntity<?> checkEamilDuplicate(@RequestParam String email){
    var result = memberService.findByEmail(email);
    return ResponseEntity.ok(result);
  }

/**
 * 회원가입
 */
  @PostMapping("/signup")
  public ResponseEntity<?> memberRegister(@RequestBody SignUp request){
    var member = memberService.memberRegister(request);

    return ResponseEntity.ok(member);
  }

  /**
   * 회원가입 후 이메일 인증
   *
   */
  @GetMapping("/email-auth")
  public ResponseEntity<?> memberEmailAuth(@RequestParam String key, @RequestParam String id){
    var result = memberService.memberEmailAuthCheck(id, key);

    return ResponseEntity.ok(result);
  }
}
