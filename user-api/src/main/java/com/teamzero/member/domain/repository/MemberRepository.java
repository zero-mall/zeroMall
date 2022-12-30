package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberIdAndEmail(Long memberId, String email);
    boolean exsitsByEmail(String email);
    boolean exsitsByEmailAndEmailAuthKey(String email, String emailAuthKey);
    Optional<MemberEntity> findByEmail(String email);

    Page<MemberEntity> findAll(Pageable pageable);

    boolean existsByEmail(String email);

}
