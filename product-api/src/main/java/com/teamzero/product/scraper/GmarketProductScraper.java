package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.ProductOfMallEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GmarketProductScraper implements ProductScraperInterface {

    private static final String SEARCH_URL = "https://browse.gmarket.co.kr/search?keyword=";
    private static final double TOLERANCE = 0.05;


    @Override
    public List<ProductOfMallEntity> getScrapProductList(ProductEntity product) {
        return null;
    }
}
