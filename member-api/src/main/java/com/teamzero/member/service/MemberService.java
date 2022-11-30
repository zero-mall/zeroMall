package com.teamzero.member.service;

import com.teamzero.member.domain.model.Member;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByMemberIdAndEmail(Long memberId, String email) {
        return memberRepository.findByMemberIdAndEmail(memberId, email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
    }

}
