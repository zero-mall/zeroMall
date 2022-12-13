package com.teamzero.product.domain.dto;

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
public class MallProduct {

  private String mallName;
  private Long mallProductId;
  private String mallProductName;

  public static MallProduct fromEntity(MallProductEntity mallProduct) {
    return MallProduct.builder()
        .mallName(mallProduct.getMall().getName())
        .mallProductId(mallProduct.getId())
        .mallProductName(mallProduct.getName())
        .build();
  }

}
