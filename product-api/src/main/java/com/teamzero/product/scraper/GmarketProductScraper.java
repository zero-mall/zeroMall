package com.teamzero.product.scraper;

import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GmarketProductScraper extends ScrapConfig implements ProductScraperInterface {

    private static final String SEARCH_URL = "https://browse.gmarket.co.kr/search?keyword=";

    @Override
    public List<MallProductEntity> getScrapProductList(ProductEntity product) {
        return null;
    }
}
