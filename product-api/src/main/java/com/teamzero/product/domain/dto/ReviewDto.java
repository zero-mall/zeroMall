package com.teamzero.product.domain.dto;

import com.teamzero.product.domain.model.ReviewEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewDto {

    /**
     * 리뷰 작성 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateReview {

        private Long productId;
        private Long memberId;
        private Long orderId;
        private String review;
        private LocalDateTime registeredAt;

        public static ReviewDto.CreateReview fromEntity(ReviewEntity review) {
            return CreateReview.builder()
                .productId(review.getProductId())
                .memberId(review.getMemberId())
                .orderId(review.getOrderId())
                .review(review.getReview())
                .registeredAt(review.getRegisteredAt())
                .build();
        }
    }

    /**
     * 리뷰 수정 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ModifyReview {

        private Long productId;
        private Long memberId;
        private Long orderId;
        private String review;
        private LocalDateTime modifiedAt;

        public static ReviewDto.ModifyReview fromEntity(ReviewEntity review) {
            return ReviewDto.ModifyReview.builder()
                .productId(review.getProductId())
                .memberId(review.getMemberId())
                .orderId(review.getOrderId())
                .review(review.getReview())
                .modifiedAt(review.getUpdatedAt())
                .build();
        }
    }

    /**
     * 리뷰 읽기 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReadReview {

        private Long reviewId;
        private Long productId;
        private Long memberId;
        private String review;
        private LocalDateTime registeredAt;
        private LocalDateTime modifiedAt;
    }


}
