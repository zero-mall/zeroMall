package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.MallProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MallProductRepository extends JpaRepository<MallProductEntity, Long> {

  Page<MallProductEntity> findAllByProductId(Pageable pageable, Long productId);

}
