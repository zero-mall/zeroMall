package com.teamzero.member.service;

import static com.teamzero.member.exception.ErrorCode.MEMBER_GRADE_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_STATUS_NOT_EXIST;
import static com.teamzero.member.exception.ErrorCode.TOKEN_NOT_VALID;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.AdminEntity;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.AdminStatus;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.AdminInfoDto;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final MemberRepository memberRepository;

    private final MemberGradeRepository memberGradeRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public void findByAdminIdAndEmail(Long adminId, String email) {
        adminRepository.findByAdminIdAndEmail(adminId, email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
    }

    /**
     * 일반 회원 목록 조회
     */
    public Page<MemberInfoDto> getMemberList(Pageable pageable){

        return memberRepository.findAll(pageable).map(MemberInfoDto::fromEntity);
    }

    /**
     * 일반 회원 상세 조회
     */
    public MemberInfoDto getMemberDetail(Long memberId) {
        return MemberInfoDto.fromEntity(memberRepository.findById(memberId)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND)));
    }

    /**
     * 일반 회원 등급 및 상태 변경
     * - 입력한 값(회원id, 등급)에서,
     *   회원이 없는 경우, 회원 등급이 없는 경우, 실패 응답
     */
    @Transactional
    public MemberInfoDto modifyMemberGradeOrStatus(ModifyDto modify) {

        MemberEntity member = memberRepository.findById(modify.getMemberId())
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));

        MemberGradeEntity memberGrade =
            memberGradeRepository.findByName(modify.getGrade())
                .orElseThrow(()
                    -> new TeamZeroException(MEMBER_GRADE_NOT_FOUND));

        if (!MemberStatus.hasStatus(modify.getStatus())) {
            throw new TeamZeroException(MEMBER_STATUS_NOT_EXIST);
        }

        member.setMemberGradeEntity(memberGrade);
        member.setMemberStatus(MemberStatus.valueOf(modify.getStatus()));

        return MemberInfoDto.fromEntity(member);

    }

    /**
     * 관리자 상태 변경
     */
    @Transactional
    public AdminInfoDto modifyAdminStatus(ModifyDto modify) {

        AdminEntity admin = adminRepository.findById(modify.getMemberId())
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
        if (!MemberStatus.hasStatus(modify.getStatus())) {
            throw new TeamZeroException(MEMBER_STATUS_NOT_EXIST);
        }
        admin.setAdminStatus(AdminStatus.valueOf(modify.getStatus()));

        return AdminInfoDto.fromEntity(admin);
    }

    public void validateToken(String token) {
        // 토큰이 유효한지 확인
        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }

        // 토큰의 요청자 이메일이 관리자인지 확인
        if (!adminRepository.existsByEmail(
            jwtAuthenticationProvider.getUserVo(token).getEmail())) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }
    }

}
