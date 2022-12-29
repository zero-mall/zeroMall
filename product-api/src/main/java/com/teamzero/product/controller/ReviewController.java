package com.teamzero.product.controller;

import static com.teamzero.member.exception.ErrorCode.TOKEN_NOT_VALID;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.domain.UserVo;
import com.teamzero.member.exception.TeamZeroException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;


    /**
     * 리뷰 등록
     */
    @PutMapping("/create")
    public ResponseEntity<CreateReview> createReview(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody ReviewDto.CreateReview create) {
        // 토큰 확인
        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }
        // 토큰에서 회원 정보 얻기
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);

        var createResult = reviewService.createReview(create, vo.getMemberId());
        return ResponseEntity.ok(createResult);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/modify")
    public ResponseEntity<ModifyReview> modifyReview(
        @RequestHeader(name = "Authentication") String token,
        @RequestBody ReviewDto.ModifyReview modify) {
        // 토큰 확인
        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }
        // 토큰에서 회원 정보 얻기
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        var modifyReview = reviewService.modifyReview(modify, vo.getMemberId());
        return ResponseEntity.ok(modifyReview);
    }

    /**
     * 리뷰 읽기
     *
     * @Param 리뷰번호
     */
    @GetMapping("/get-review")
    public ResponseEntity<ReadReview> getReview(@RequestParam Long reviewId) {
        var getReview = reviewService.getReview(reviewId);
        return ResponseEntity.ok(getReview);
    }

    /**
     * 리뷰 삭제
     *
     * @Param 주문번호, 리뷰번호, 회원번호
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteReview(
        @RequestHeader(name = "Authentication") String token,
        @RequestParam Long orderId,
        @RequestParam Long reviewId) {
        // 토큰 확인
        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new TeamZeroException(TOKEN_NOT_VALID);
        }
        // 토큰에서 회원 정보 얻기
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        var deleteReview = reviewService.deleteReview(orderId, reviewId,
            vo.getMemberId());
        return ResponseEntity.ok(deleteReview);
    }


}
