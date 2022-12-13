package com.teamzero.product.domain.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NaverSort {

  SIM("정확도순"),
  PRICE_ASC("낮은 가격순"),
  PRICE_DSC("높은 가격순"),
  DATE("등록일순");

  private final String description;

}
