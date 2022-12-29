package com.teamzero.product.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MallProductSearch {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request{
      private Long productId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response{
    private String name;
    private String imageUrl;
    private String detailUrl;
    private int price;
    private String mallName;
  }
}