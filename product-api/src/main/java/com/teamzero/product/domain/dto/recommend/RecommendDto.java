package com.teamzero.product.domain.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendDto {

  private int ageGroup;
  private Long productId;
  private String productName;
  private String imageUrl;
  private int price;
  private int maxPrice;
  private double perc;

}
