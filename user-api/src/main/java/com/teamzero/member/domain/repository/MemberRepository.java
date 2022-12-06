package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberIdAndEmail(Long memberId, String email);
    long countByEmail(String email);
    int countByEmailAndEmailAuthKey(String email, String emailAuthKey);
    Optional<MemberEntity> findByEmail(String email);

    Page<MemberEntity> findAll(Pageable pageable);

}
