package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
}
