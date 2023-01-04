package com.teamzero.product.controller;

import com.teamzero.product.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/likes")
@Api(tags = {"LikeController"}, description = "좋아요 API")
public class LikeController {

    private final LikeService likeService;


    /**
     * 좋아요 등록
     */
    @PostMapping("/add")
    @ApiOperation(value = "좋아요 등록")
    public boolean addLike(@RequestParam String email,
        @RequestParam Long productId) {

        return likeService.addLike(email, productId);

    }

    /**
     * 좋아요 조회
     */
    @PostMapping("/view")
    @ApiOperation(value = "좋아요 조회")
    public Long getLikes(
        @RequestParam Long productId) {

        return likeService.countLikes(productId);

    }

    /**
     * 좋아요 취소
     */
    @PostMapping("/cancel")
    @ApiOperation(value = "좋아요 취소")
    public boolean cancelLike(@RequestParam String email,
        @RequestParam Long productId) {
        return likeService.cancelLike(email, productId);

    }


}
