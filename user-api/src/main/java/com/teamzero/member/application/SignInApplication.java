package com.teamzero.member.application;

import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.util.UserApiUtils.checkEncPassword;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.model.AdminEntity;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.dto.SignInDto;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final MemberRepository memberRepository;

    private final AdminRepository adminRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 일반 회원 JWT 발행
     */
    public String memberSignInToken(SignInDto signIn) {

        // 로그인 가능 여부 (아이디, 비밀번호와 일치하는 계정 확인)
        MemberEntity member = memberRepository.findAllByEmail(signIn.getEmail())
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));

        if (!checkEncPassword(signIn.getPassword(), member.getPassword())) {
            throw new TeamZeroException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 토큰 생성 및 응답
        return jwtAuthenticationProvider.createToken(member.getMemberId(),
            member.getEmail(), member.getMemberGradeEntity().getName());
    }

    /**
     * 관리자 JWT 발행
     */
    public String adminSignInToken(SignInDto signIn) {

        // 로그인 가능 여부 (아이디, 비밀번호와 일치하는 계정 확인)
        AdminEntity admin = adminRepository.findByEmail(signIn.getEmail())
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));

        if (!checkEncPassword(signIn.getPassword(), admin.getPassword())) {
            throw new TeamZeroException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 토큰 생성 및 응답
        return jwtAuthenticationProvider.createToken(
            admin.getAdminId(), admin.getEmail());
    }

}
