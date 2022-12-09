package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGradeRepository extends JpaRepository<MemberGradeEntity, Long> {
    
    Optional<MemberGradeEntity> findByName(String name);
    
}
