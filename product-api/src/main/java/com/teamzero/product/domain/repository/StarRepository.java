package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.StarEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StarRepository extends JpaRepository<StarEntity, Long> {

  Optional<StarEntity> findByProductIdAndMemberEmail(Long productId, String memberEmail);

  long countAllByProductId(Long productId);

  @Query("select avg(s.score) from StarEntity s where s.productId = :productId")
  double calAvgStarByProductId(long productId);

}
