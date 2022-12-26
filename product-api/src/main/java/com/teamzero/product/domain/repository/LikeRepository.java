package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.LikeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByMemberEmailAndProductId(String email,
        Long productId);

    long countAllByProductId(Long productId);


}
