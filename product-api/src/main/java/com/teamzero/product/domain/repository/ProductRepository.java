package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  Optional<ProductEntity> findByNaverId(String naverId);

  boolean existsByProductId(Long productId);
  List<ProductEntity> findAll();

  Optional<ProductEntity> findByCatId(String catId);

  @Query("select avg(p.viewCount) from ProductEntity p")
  double getTotalAvgViewCount();

}
