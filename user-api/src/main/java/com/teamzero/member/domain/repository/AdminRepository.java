package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.AdminEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByEmail(String email);

    Optional<AdminEntity> findByAdminIdAndEmail(Long adminId, String email);

    boolean existsByEmail(String email);
}
