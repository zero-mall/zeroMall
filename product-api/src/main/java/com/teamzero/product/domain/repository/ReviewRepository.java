package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    ReviewEntity findByReviewId(Long reviewId);

    ReviewEntity findByOrderIdAndMemberIdAndProductId(Long orderId, Long memberId, Long productId);

    boolean existsByOrderIdAndMemberIdAndProductId(Long orderId, Long memberId, Long productId);

    boolean existsByReviewId(Long reviewId);


}
