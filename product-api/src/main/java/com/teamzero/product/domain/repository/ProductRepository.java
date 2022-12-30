package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.ProductEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  Optional<ProductEntity> findByNaverId(String naverId);

  boolean existsByProductId(Long productId);
  List<ProductEntity> findAll();

  List<ProductEntity> findAllByCatId(String catId);

  List<ProductEntity> findAllByRegisteredAtAfterOrModifiedAtAfter(
        LocalDateTime monthAgo, LocalDateTime monthAgo2);


}
