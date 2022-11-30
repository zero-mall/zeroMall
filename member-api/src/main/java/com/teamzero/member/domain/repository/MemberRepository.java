package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberIdAndEmail(Long memberId, String email);
}
