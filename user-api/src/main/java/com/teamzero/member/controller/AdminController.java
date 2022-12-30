package com.teamzero.member.controller;

import com.teamzero.member.domain.model.dto.AdminInfoDto;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
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


    /**
     * 일반 회원 목록 조회
     */
    @GetMapping("/member/list")
    public ResponseEntity<Page<MemberInfoDto>> getMemberList(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody PageRequest pageRequest){

        adminService.validateToken(token);

        return ResponseEntity.ok(adminService.getMemberList(pageRequest));
    }

    /**
     * 일반 회원 상세 조회
     */
    @GetMapping("/member/detail/{id}")
    public ResponseEntity<MemberInfoDto> getMemberDetail(
        @RequestHeader(name = "Authentication") String token,
        @PathVariable Long id){

        adminService.validateToken(token);

        return ResponseEntity.ok(adminService.getMemberDetail(id));
    }

    /**
     * 일반 회원 등급 및 상태 변경
     * - 회원 등급 또는 상태 수정, 또는 둘 다 수정
     */
    @PutMapping("/member/detail/modify")
    public ResponseEntity<MemberInfoDto> modifyMemberGradeOrStatus(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody ModifyDto modify){

        adminService.validateToken(token);

        return ResponseEntity.ok(adminService.modifyMemberGradeOrStatus(modify));
    }

    /**
     * 관리자 상태 변경
     * - 관련 정책 : 관리 계정은 영구 삭제하지 않고 필요시 정지만 하도록 한다.
     *   ( 차후 서비스가 확장되면 일괄적으로 관리자 회원 계정 삭제 )
     */
    @PutMapping("/admin/detail/modify")
    public ResponseEntity<AdminInfoDto> modifyAdminStatus(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody ModifyDto modify){

        adminService.validateToken(token);

        return ResponseEntity.ok(adminService.modifyAdminStatus(modify));
    }


}
