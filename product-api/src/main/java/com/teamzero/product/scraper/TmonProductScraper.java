package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TmonProductScraper implements ProductScraperInterface {

  private static final String SEARCH_URL = "https://search.tmon.co.kr/search/?keyword=%s";
  private static final double TOLERANCE  = 0.05;

  @Override
  public List<ProductEntity> getScrapProductList(String productName) {
    return null;
  }

}
