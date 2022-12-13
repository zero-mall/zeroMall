package com.teamzero.product.domain.dto;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSet {

  private Set<ProductEntity> products;

  public void addProduct(ProductEntity product) {
    if (this.products == null) {
      products = new HashSet();
    }
    products.add(product);
  }

}
