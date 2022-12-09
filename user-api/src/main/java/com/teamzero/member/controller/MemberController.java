package com.teamzero.member.controller;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.domain.UserVo;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        UserVo vo = jwtAuthenticationProvider.getUserVo(token);

        MemberEntity member = memberService.findByMemberIdAndEmail(vo.getMemberId(), vo.getEmail());

        return ResponseEntity.ok(MemberInfo.fromEntity(member));
    }

}
