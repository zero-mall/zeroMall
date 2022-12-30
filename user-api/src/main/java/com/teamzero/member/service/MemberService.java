package com.teamzero.member.service;

import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.util.UserApiUtils.createEncPassword;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberEntity findByMemberIdAndEmail(Long memberId, String email) {
        return memberRepository.findByMemberIdAndEmail(memberId, email)
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public MemberInfoDto modify(ModifyDto member) {
        MemberEntity memberEntity = memberRepository.findById(
            member.getMemberId()).orElseThrow(()
                -> new TeamZeroException(MEMBER_NOT_FOUND));

        String encPassword = createEncPassword(member.getPassword());
        memberEntity.setNickname(member.getNickname());
        memberEntity.setAge(member.getAge());
        memberEntity.setPassword(encPassword);

        return MemberInfoDto.fromEntity(memberEntity);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public boolean withdraw(String email) {

        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));

        memberEntity.setMemberStatus(MemberStatus.WITHDRAW);

        return true;
    }


}
