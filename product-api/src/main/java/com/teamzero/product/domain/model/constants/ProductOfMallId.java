package com.teamzero.product.domain.model.constants;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOfMallId implements Serializable {
  private long productId;
  private String mallId;
}
