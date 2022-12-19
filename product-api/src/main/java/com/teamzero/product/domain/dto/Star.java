package com.teamzero.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Star {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        // 상품 정보
        private Long productId;

        // 별점
        private int score;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{

        // 상품 정보
        private Long productId;

        // 상품의 별점 정보
        private double avgStar;  // 평균 별점
        private long   count;    // 별점 등록자 수

    }

}
