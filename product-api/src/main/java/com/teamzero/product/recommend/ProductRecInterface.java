package com.teamzero.product.recommend;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;

public interface ProductRecInterface {

  List<ProductEntity> recommendProducts();

}
