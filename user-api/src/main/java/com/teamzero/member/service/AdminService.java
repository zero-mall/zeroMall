package com.teamzero.member.service;

import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_SIGNUP_EMAIL_DUPLICATE;

import com.teamzero.member.domain.model.AdminEntity;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.AdminStatus;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.AdminInfo;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.domain.model.dto.SignUp;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final MemberRepository memberRepository;

    private final MemberGradeRepository memberGradeRepository;


    /**
     * 관리자 조회
     */
    public Optional<AdminEntity> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

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
    public MemberInfo modifyMemberGradeOrStatus(Modify modify) {

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

    /**
     * Admin 회원가입
     * AdminStatus 는 계정 생성시 IN_USE를 default로
     * 가입시 해당 이메일 존재하는경우 실패 응답
     */
    @Transactional
    public AdminInfo adminRegister(SignUp singUp) {
        if (!adminRepository.existsByEmail(singUp.getEmail())) {
            throw new TeamZeroException(MEMBER_SIGNUP_EMAIL_DUPLICATE);
        }
        String encPassword = BCrypt.hashpw(singUp.getPassword(), BCrypt.gensalt());
        var admin = AdminEntity.builder()
            .email(singUp.getEmail())
            .password(encPassword)
            .adminStatus(AdminStatus.IN_USE)
            .build();

        adminRepository.save(admin);
        return AdminInfo.fromEntity(admin);
    }

    /**
     * 관리자 상태 변경
     */
    @Transactional
    public AdminInfo modifyAdminStatus(Modify modify) {

        AdminEntity admin = adminRepository.findById(modify.getMemberId())
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
        if (!MemberStatus.hasStatus(modify.getStatus())) {
            new TeamZeroException(ErrorCode.MEMBER_STATUS_NOT_EXIST);
        }
        admin.setAdminStatus(AdminStatus.valueOf(modify.getStatus()));

        return AdminInfo.fromEntity(admin);

    }

}
