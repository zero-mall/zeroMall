package com.teamzero.member.controller;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
import com.teamzero.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"MemberController"}, description = "나의 정보 조회/수정/탈퇴")
public class MemberController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final MemberService memberService;

    /**
     * 회원 수정
     */
    @PutMapping("/modify")
    @ApiOperation(value = "회원 수정")
    public ResponseEntity<MemberInfoDto> modify(
        @RequestBody ModifyDto member) {
        return ResponseEntity.ok(memberService.modify(member));
    }

    /**
     * 회원 탈퇴
     */
    @PutMapping("/withdraw")
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<Boolean> withdraw(@RequestParam String email) {
        return ResponseEntity.ok(memberService.withdraw(email));
    }

    /**
     * 회원 조회
     * (JWT에서 조회)
     */
    @GetMapping("/info")
    @ApiOperation(value = "회원 조회")
    public ResponseEntity<MemberInfoDto> getInfo(
        @RequestHeader(name = "X-AUTH-TOKEN") String token){

        var vo = jwtAuthenticationProvider.getUserVo(token);

        var member = memberService.findByMemberIdAndEmail(
            vo.getMemberId(), vo.getEmail());

        return ResponseEntity.ok(MemberInfoDto.fromEntity(member));
    }

}
