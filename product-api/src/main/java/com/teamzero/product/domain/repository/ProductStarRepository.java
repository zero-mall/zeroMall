package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.StarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStarRepository extends JpaRepository<StarEntity, Long> {



}
