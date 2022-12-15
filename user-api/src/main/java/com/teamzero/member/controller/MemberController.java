package com.teamzero.member.controller;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> modifyMember(@RequestBody Modify member) {
        var modifyResult = memberService.modifyMember(member);

        return ResponseEntity.ok(modifyResult);
    }

    /**
     * 회원 탈퇴
     */
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdrawMember(@RequestBody Modify member) {
        var withdrawResult = memberService.withdrawMember(member);
        return ResponseEntity.ok(withdrawResult);
    }

    /**
     * 회원 조회
     * (JWT에서 조회)
     */
    @GetMapping("/info")
    public ResponseEntity<MemberInfo> getMemberInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token){

        var vo = jwtAuthenticationProvider.getUserVo(token);

        var member = memberService.findByMemberIdAndEmail(vo.getMemberId(), vo.getEmail());

        return ResponseEntity.ok(MemberInfo.fromEntity(member));
    }

}
