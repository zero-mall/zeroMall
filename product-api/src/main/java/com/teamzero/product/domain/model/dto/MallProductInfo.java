package com.teamzero.product.domain.model.dto;

import com.teamzero.product.domain.model.MallProductInfoEntity;
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

  public static MallProductInfo fromEntity(MallProductInfoEntity mallProductInfo) {
    return MallProductInfo.builder()
        .mallName(mallProductInfo.getMallInfo().getName())
        .mallProductId(mallProductInfo.getId())
        .mallProductName(mallProductInfo.getName())
        .build();
  }

}
