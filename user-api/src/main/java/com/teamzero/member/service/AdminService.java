package com.teamzero.member.service;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final MemberRepository memberRepository;

    private final MemberGradeRepository memberGradeRepository;

    public void findByAdminIdAndEmail(Long adminId, String email) {
        adminRepository.findByAdminIdAndEmail(adminId, email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
    }

    /**
     * 일반 회원 목록 조회
     */
    public Page<MemberInfo> getMemberList(Pageable pageable){

        return memberRepository.findAll(pageable).map(MemberInfo::fromEntity);
    }

    /**
     * 일반 회원 상세 조회
     */
    public MemberInfo getMemberDetail(Long memberId) {
        return MemberInfo.fromEntity(memberRepository.findById(memberId)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND)));
    }

    /**
     * 일반 회원 등급 및 상태 변경
     * - 입력한 값(회원id, 등급)에서,
     *   회원이 없는 경우, 회원 등급이 없는 경우, 실패 응답
     */
    @Transactional
    public MemberInfo updateMemberGradeOrStatus(Modify modify) {

        MemberEntity member = memberRepository.findById(modify.getMemberId())
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));

        MemberGradeEntity memberGrade = memberGradeRepository.findByName(modify.getGrade())
                .orElseThrow(() -> new TeamZeroException(ErrorCode.MEMBER_GRADE_NOT_FOUND));

        if (!MemberStatus.hasStatus(modify.getStatus())) {
            new TeamZeroException(ErrorCode.MEMBER_STATUS_NOT_EXIST);
        }

        member.setMemberGradeEntity(memberGrade);
        member.setMemberStatus(MemberStatus.valueOf(modify.getStatus()));

        return MemberInfo.fromEntity(member);

    }
}
