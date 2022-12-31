package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.MallEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallRepository extends JpaRepository<MallEntity, Long> {

  Optional<MallEntity> findAllBymallId(Long mallId);

  Optional<MallEntity> findByName(String name);

  boolean existsBySchedulerYn(boolean isTrue);

}
