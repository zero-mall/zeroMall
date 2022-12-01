package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeRepository extends JpaRepository<MemberGradeEntity, Long> {
}
