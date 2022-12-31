package com.teamzero.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDto {

  private String catId;
  private long catViewCount;
  private double catViewAvg;
  private Long productId;
  private String productName;
  private long productViewCount;

}
