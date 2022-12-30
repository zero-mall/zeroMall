package com.teamzero.member.controller;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
import com.teamzero.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final MemberService memberService;

    /**
     * 회원 수정
     */
    @PutMapping("/modify")
    public ResponseEntity<MemberInfoDto> modify(
        @RequestBody ModifyDto member) {
        return ResponseEntity.ok(memberService.modify(member));
    }

    /**
     * 회원 탈퇴
     */
    @PutMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@RequestParam String email) {
        return ResponseEntity.ok(memberService.withdraw(email));
    }

    /**
     * 회원 조회
     * (JWT에서 조회)
     */
    @GetMapping("/info")
    public ResponseEntity<MemberInfoDto> getInfo(
        @RequestHeader(name = "X-AUTH-TOKEN") String token){

        var vo = jwtAuthenticationProvider.getUserVo(token);

        var member = memberService.findByMemberIdAndEmail(
            vo.getMemberId(), vo.getEmail());

        return ResponseEntity.ok(MemberInfoDto.fromEntity(member));
    }

}
