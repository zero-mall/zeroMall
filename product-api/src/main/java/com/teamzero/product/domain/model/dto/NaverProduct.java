package com.teamzero.product.domain.model.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaverProduct {

  // 네이버 상품 정보
  // 카테고리
  private String category1; // 대분류
  private String category2; // 중분류
  private String category3; // 소분류
  
  // 기본 정보
  private Long naverId;
  private String title;
  private String brand;
  private String imageUrl;
  private int lPrice;

  public static NaverProduct parseJson(JsonObject jsonObject) {
    return NaverProduct.builder()
        .category1(jsonObject.get("category1").getAsString())
        .category2(jsonObject.get("category2").getAsString())
        .category3(jsonObject.get("category3").getAsString())
        .naverId(Long.parseLong(jsonObject.get("productId").getAsString()))
        .title(jsonObject.get("title").getAsString())
        .brand(jsonObject.get("brand").getAsString())
        .imageUrl(jsonObject.get("image").getAsString())
        .lPrice(Integer.parseInt(jsonObject.get("lprice").getAsString()))
        .build();
  }
}
