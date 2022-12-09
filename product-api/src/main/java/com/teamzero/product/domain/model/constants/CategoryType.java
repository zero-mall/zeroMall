package com.teamzero.product.domain.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
  ATYPE("atype"), //대분류
  BTYPE("btype"), //중분류
  CTYPE("ctype"); //소분류

  private final String description;
}
