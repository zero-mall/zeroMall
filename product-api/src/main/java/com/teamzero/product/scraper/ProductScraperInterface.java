package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.util.List;

public interface ProductScraperInterface {

//스크래핑하여 List<productEntity>로 반환
  List<MallProductEntity> getScrapProductList(ProductEntity product);

  // TODO 의논 필요
  // List<MallProductEntity> getScrapProductList(ProductEntity product, MallEntity mall);
}
