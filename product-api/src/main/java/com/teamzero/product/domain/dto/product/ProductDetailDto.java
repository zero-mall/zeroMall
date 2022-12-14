package com.teamzero.product.domain.dto.product;

import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductDetailDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request{

    // 네이버 상품 정보
    // 카테고리
    private String category1; // 대분류
    private String category2; // 중분류
    private String category3; // 소분류

    // 기본 정보
    private String naverId;
    private String title;
    private String brand;
    private String imageUrl;
    private int lPrice;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response{

    private Long productId;
    private String catId;
    private String naverId;
    private String brand;
    private String name;
    private String imageUrl;
    private int price;
    private List<MallEntity> mallProducts;
    private double avgStar;
    private long viewCount;
    private long likeCount;

    public static ProductDetailDto.Response fromEntity(ProductEntity product) {
      return ProductDetailDto.Response.builder()
          .productId(product.getProductId())
          .catId(product.getCatId())
          .naverId(product.getNaverId())
          .brand(product.getBrand())
          .name(product.getProductName())
          .imageUrl(product.getImageUrl())
          .price(product.getPrice())
          .viewCount(product.getViewCount())
          .likeCount(product.getLikeCount())
          .build();

    }

  }

}