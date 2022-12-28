package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.MallProductEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallProductRepository extends JpaRepository<MallProductEntity, Long> {
  Optional<MallProductEntity> findAllByProductMallIdAndMallId
      (String productMallId, Long mallId);
  Page<MallProductEntity> findAllByProductId(Pageable pageable, Long productId);

}
