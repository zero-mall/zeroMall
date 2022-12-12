package com.teamzero.product.service;

import com.teamzero.product.domain.dto.ReviewDto;
import com.teamzero.product.domain.dto.ReviewDto.ReadReview;
import com.teamzero.product.domain.model.ReviewEntity;
import com.teamzero.product.domain.repository.ReviewRepository;
import com.teamzero.product.exception.ErrorCode;
import com.teamzero.product.exception.TeamZeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 등록
     */
    @Transactional
    public ReviewDto.CreateReview createReview(ReviewDto.CreateReview create) {

        // 해당 '상품'의 '주문'에 대한 '사용자'의 리뷰가 존재하는지 확인 후 존재하면 에러발생
        if (reviewRepository.existsByOrderIdAndMemberIdAndProductId(create.getOrderId(),
            create.getMemberId(), create.getProductId())) {
            throw new TeamZeroException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // ReviewEntity 입력 후 ReviewRepository 에 저장
        ReviewEntity review = ReviewEntity.builder()
            .productId(create.getProductId())
            .memberId(create.getMemberId())
            .orderId(create.getOrderId())
            .review(create.getReview())
            .build();
        reviewRepository.save(review);

        //결과 리턴
        return ReviewDto.CreateReview.fromEntity(review);

    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewDto.ModifyReview modifyReview(ReviewDto.ModifyReview modify) {
        // 해당 '상품'의 '주문'에 대한 '사용자'의 리뷰가 존재하는지 확인 후 존재하지 않으면 에러발생
        if (!reviewRepository.existsByOrderIdAndMemberIdAndProductId(modify.getOrderId(),
            modify.getMemberId(), modify.getProductId())) {
            throw new TeamZeroException(ErrorCode.REVIEW_DOES_NOT_EXISTS);
        }

        // ReviewRepository 에서 주문번호,사용자번호,상품번호 통하여 불러온 후 리뷰내용과 수정시간 입력
        ReviewEntity review =
            reviewRepository.findByOrderIdAndMemberIdAndProductId(modify.getOrderId(),
                modify.getMemberId(), modify.getProductId());
        review.setReview(modify.getReview());

        //결과 리턴
        return ReviewDto.ModifyReview.fromEntity(review);
    }

    /**
     * 리뷰 읽기
     */
    @Transactional
    public ReadReview getReview(Long reviewId) {
        // 해당 '리뷰'가 존재하는지 확인 후 존재하지 않으면 에러발생
        if (!reviewRepository.existsByReviewId(reviewId)) {
            throw new TeamZeroException(ErrorCode.REVIEW_DOES_NOT_EXISTS);
        }
        // ReviewRepository 에서 ReviewId로 찾은 후 ReviewDto 통해서 Return
        // 주문번호는 노출 불필요하다 판단하여 노출하지않음.
        ReviewEntity review = reviewRepository.findByReviewId(reviewId);
        ReadReview readReview = ReadReview.builder()
            .reviewId(review.getReviewId())
            .memberId(review.getMemberId())
            .productId(review.getProductId())
            .review(review.getReview())
            .modifiedAt(review.getUpdatedAt())
            .registeredAt(review.getRegisteredAt())
            .build();
        return readReview;
    }


    /**
     * 리뷰 삭제
     */
    @Transactional
    public boolean deleteReview(Long orderId, Long reviewId, Long memberId) {
        // 해당 '주문'에 대한 '사용자'의 '리뷰'가 존재하는지 확인 후 존재하지 않으면 에러발생
        if (!reviewRepository.existsByOrderIdAndMemberIdAndProductId(orderId, memberId, reviewId)) {
            throw new TeamZeroException(ErrorCode.REVIEW_DOES_NOT_EXISTS);
        }
        // 주문번호, 사용자아이디, 리뷰아이디로 Entity 삭제
        ReviewEntity review = reviewRepository.findByOrderIdAndMemberIdAndProductId(orderId,
            memberId, reviewId);
        reviewRepository.delete(review);

        //성공시 true 반환
        return true;
    }
}
