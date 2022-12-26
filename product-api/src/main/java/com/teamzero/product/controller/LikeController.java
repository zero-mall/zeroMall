package com.teamzero.product.controller;

import com.teamzero.product.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/like")
public class LikeController {

    private final LikeService likeService;


    /**
     * 좋아요 등록
     */
    @GetMapping
    public boolean addLike(@RequestParam String email,
        @RequestParam Long productId) {

        return likeService.addLike(email, productId);

    }

    /**
     * 좋아요 조회
     */
    @GetMapping
    public Long getLikes(
        @RequestParam Long productId) {

        return likeService.likeCount(productId);

    }

    /**
     * 좋아요 취소
     */
    @PutMapping
    public boolean cancelLike(@RequestParam String email,
        @RequestParam Long productId) {
        return likeService.cancelLike(email, productId);

    }


}
