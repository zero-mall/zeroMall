package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberGradeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberGradeRepository extends JpaRepository<MemberGradeEntity, Long> {
    Optional<MemberGradeEntity> findByName(String name);
    
}
