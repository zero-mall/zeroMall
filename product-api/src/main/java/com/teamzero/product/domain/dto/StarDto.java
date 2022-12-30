package com.teamzero.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StarDto {

    // 상품 정보
    private Long productId;

    // 상품의 별점 정보
    private double starAvg;  // 평균 별점
    private long starCount;    // 별점 등록자 수

}
