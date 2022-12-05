package com.teamzero.member.controller;

import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /** TODO
     * 관리자 회원 정지
     * - 정책 : 계정을 삭제하지 않고, 회원 정지만 한다.
     *   ( 차후 서비스가 확장되면 일괄적으로 관리자 회원 계정 삭제 )
     */

    /**
     * 일반 회원 목록 조회
     */
    @GetMapping("/member/list")
    public ResponseEntity<Page<MemberInfo>> getMemberList(@RequestBody PageRequest pageRequest){
        return ResponseEntity.ok(adminService.getMemberList(pageRequest));
    }

    /**
     * 일반 회원 상세 조회
     */
    @GetMapping("/member/detail/{id}")
    public ResponseEntity<MemberInfo> getMemberDetail(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getMemberDetail(id));
    }

    /**
     * 일반 회원 등급 및 상태 변경
     * - 회원 등급 또는 상태 수정, 또는 둘 다 수정
     */
    @PutMapping("/member/detail/update")
    public ResponseEntity<MemberInfo> updateMemberGradeOrStatus(@RequestBody Modify modify){
        return ResponseEntity.ok(adminService.updateMemberGradeOrStatus(modify));
    }

}
