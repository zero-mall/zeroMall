package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;

public interface ProductScraperInterface {

  List<MallProductEntity> getScrapProductList(ProductEntity product);

  String getMallName();

}
