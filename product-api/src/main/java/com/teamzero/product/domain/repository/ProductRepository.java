package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  boolean existsByProductId(Long productId);

  Optional<ProductEntity> findByProductId(Long firstProductId);

  Optional<ProductEntity> findByNaverId(Long naverId);
}