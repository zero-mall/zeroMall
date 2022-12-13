package com.teamzero.product.domain.model.dto;

import com.teamzero.product.domain.model.MallProductEntity;
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
public class MallProductInfo {

  private String mallName;
  private Long mallProductId;
  private String mallProductName;

  public static MallProductInfo fromEntity(MallProductEntity mallProduct) {
    return MallProductInfo.builder()
        .mallName(mallProduct.getMall().getName())
        .mallProductId(mallProduct.getId())
        .mallProductName(mallProduct.getName())
        .build();
  }

}
