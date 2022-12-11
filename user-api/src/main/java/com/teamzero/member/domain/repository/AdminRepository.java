package com.teamzero.member.domain.repository;

import com.teamzero.member.domain.model.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByEmail(String email);

    Optional<AdminEntity> findByAdminIdAndEmail(Long adminId, String email);

    boolean existsByEmail(String email);
}
