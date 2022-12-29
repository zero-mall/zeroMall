package com.teamzero.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.teamzero.product.domain.dto.ReviewDto;
import com.teamzero.product.domain.dto.ReviewDto.ReadReview;
import com.teamzero.product.domain.model.ReviewEntity;
import com.teamzero.product.domain.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;


    @Test
    @DisplayName("리뷰 작성")
    void createReview() {
        //given
        Long memberId = 1L;
        ReviewDto.CreateReview review = ReviewDto.CreateReview.builder()
            .productId(1L)
            .orderId(1L)
            .review("상품이 정말 좋습니다.")
            .build();

        given(reviewRepository.existsByOrderIdAndMemberIdAndProductId(anyLong(),
            anyLong(), anyLong()))
            .willReturn(false);
        //when
        ReviewDto.CreateReview createReview = reviewService.createReview(review,
            memberId);

        //then
        assertEquals(review.getOrderId(), createReview.getOrderId());
        assertEquals(review.getProductId(), createReview.getProductId());
        assertEquals(review.getReview(), createReview.getReview());

    }

    @Test
    @DisplayName("리뷰 수정")
    void modifyReview() {
        // given
        Long memberId = 1L;
        ReviewDto.ModifyReview modifyReview = ReviewDto.ModifyReview.builder()
            .productId(1L)
            .orderId(1L)
            .review("상품이 정말 개좋습니다.")
            .build();
        given(
            reviewRepository.existsByOrderIdAndMemberIdAndProductId(1L, 1L, 1L))
            .willReturn(true);
        given(reviewRepository.findByOrderIdAndMemberIdAndProductId(1L, 1L, 1L))
            .willReturn(ReviewEntity.builder()
                .memberId(1L)
                .productId(1L)
                .orderId(1L)
                .review("상품이 정말 좋습니다.").build());

        //when
        ReviewDto.ModifyReview modifiedReview = reviewService.modifyReview(
            modifyReview,memberId);

        //then
        assertEquals(modifyReview.getProductId(),
            modifiedReview.getProductId());
        assertEquals(modifyReview.getReview(), modifiedReview.getReview());

    }

    @Test
    @DisplayName("리뷰 읽기")
    void getReview() {
        // given
        given(reviewRepository.existsByReviewId(1L))
            .willReturn(true);
        given(reviewRepository.findByReviewId(1L))
            .willReturn(ReviewEntity.builder()
                .reviewId(1L)
                .memberId(1L)
                .productId(1L)
                .review("상품이 정말 좋습니다.").build());

        //when
        ReadReview ReadReview = reviewService.getReview(1L);

        //then
        assertEquals(1L, ReadReview.getMemberId());
        assertEquals(1L, ReadReview.getProductId());
        assertEquals("상품이 정말 좋습니다.", ReadReview.getReview());

    }


    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() {
        // given
        given(
            reviewRepository.existsByOrderIdAndMemberIdAndProductId(1L, 1L, 1L))
            .willReturn(true);
        given(reviewRepository.findByOrderIdAndMemberIdAndProductId(1L, 1L, 1L))
            .willReturn(ReviewEntity.builder()
                .reviewId(1L)
                .memberId(1L)
                .productId(1L)
                .review("상품이 정말 좋습니다.").build());

        //when
        boolean readReview = reviewService.deleteReview(1L, 1L, 1L);

        //then
        assertEquals(true, readReview);

    }

}