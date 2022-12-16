package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.ProductOfMallEntity;
import java.util.List;

public interface ProductScraperInterface {

//스크래핑하여 List<productEntity>로 반환
  List<ProductOfMallEntity> getScrapProductList(ProductEntity product);
}
