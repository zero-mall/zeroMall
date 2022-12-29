package com.teamzero.member.controller;

import static com.teamzero.member.exception.ErrorCode.TOKEN_NOT_VALID;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.dto.AdminInfo;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.exception.TeamZeroException;
import com.teamzero.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 일반 회원 목록 조회
     */
    @GetMapping("/member/list")
    public ResponseEntity<Page<MemberInfo>> getMemberList(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody PageRequest pageRequest){

        validateToken(token);

        return ResponseEntity.ok(adminService.getMemberList(pageRequest));
    }

    /**
     * 일반 회원 상세 조회
     */
    @GetMapping("/member/detail/{id}")
    public ResponseEntity<MemberInfo> getMemberDetail(
        @RequestHeader(name = "Authentication") String token,
        @PathVariable Long id){

        validateToken(token);

        return ResponseEntity.ok(adminService.getMemberDetail(id));
    }

    /**
     * 일반 회원 등급 및 상태 변경
     * - 회원 등급 또는 상태 수정, 또는 둘 다 수정
     */
    @PutMapping("/member/detail/modify")
    public ResponseEntity<MemberInfo> modifyMemberGradeOrStatus(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody Modify modify){

        validateToken(token);

        return ResponseEntity.ok(adminService.modifyMemberGradeOrStatus(modify));
    }

    /**
     * 관리자 상태 변경
     * - 관련 정책 : 관리 계정은 영구 삭제하지 않고 필요시 정지만 하도록 한다.
     *   ( 차후 서비스가 확장되면 일괄적으로 관리자 회원 계정 삭제 )
     */
    @PutMapping("/admin/detail/modify")
    public ResponseEntity<AdminInfo> modifyAdminStatus(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody Modify modify){

        validateToken(token);

        return ResponseEntity.ok(adminService.modifyAdminStatus(modify));
    }

    private void validateToken(String token) {
        // 토큰이 유효한지 확인
        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }

        // 토큰의 요청자 이메일이 관리자인지 확인
        if (!adminService.existByEmail(
            jwtAuthenticationProvider.getUserVo(token).getEmail())) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }
    }

}
