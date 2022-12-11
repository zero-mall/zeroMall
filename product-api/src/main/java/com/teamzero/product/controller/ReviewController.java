package com.teamzero.product.controller;

import com.teamzero.product.domain.dto.ReviewDto;
import com.teamzero.product.domain.dto.ReviewDto.CreateReview;
import com.teamzero.product.domain.dto.ReviewDto.ModifyReview;
import com.teamzero.product.domain.dto.ReviewDto.ReadReview;
import com.teamzero.product.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     */
    @PutMapping("/create")
    public ResponseEntity<CreateReview> createReview(@RequestBody ReviewDto.CreateReview create) {
        var createResult = reviewService.createReview(create);
        return ResponseEntity.ok(createResult);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/modify")
    public ResponseEntity<ModifyReview> modifyReview(@RequestBody ReviewDto.ModifyReview modify) {
        var modifyReview = reviewService.modifyReview(modify);
        return ResponseEntity.ok(modifyReview);
    }

    /**
     * 리뷰 읽기
     * @Param 리뷰번호
     */
    @GetMapping("/get-review")
    public ResponseEntity<ReadReview> getReview(@RequestParam Long reviewId) {
        var getReview = reviewService.getReview(reviewId);
        return ResponseEntity.ok(getReview);
    }

    /**
     * 리뷰 삭제
     * @Param 주문번호, 리뷰번호, 회원번호
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteReview(@RequestParam Long orderId,
        @RequestParam Long reviewId,
        @RequestParam Long memberId) {
        var deleteReview = reviewService.deleteReview(orderId, reviewId, memberId);
        return ResponseEntity.ok(deleteReview);
    }


}
