package com.teamzero.product.scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WeMakeScraper extends ScrapConfig implements ProductScraperInterface {

  private static final String SEARCH_URL = "https://search.wemakeprice.com/api/wmpsearch/api/v3.0/wmp-search/search.json"
      + "?searchType=DEFAULT&search_cate=top&keyword=%s&isRec=1&_service=5&_type=3&price=%d~%d";

  private static final double TOLERANCE  = 0.05;

  @Override
  public List<MallProductEntity> getScrapProductList(ProductEntity product) {

    List<MallProductEntity> mallProducts = new ArrayList<>();

    // 1. 기준 상품에서 검색할 정보 가져오기
    String keyword = product.getProductName();
    int price = product.getPrice();
    int minPrice = (int) Math.ceil(price - (TOLERANCE * price));
    int maxPrice = (int) Math.ceil(price + (TOLERANCE * price));

    // 2. Jsoup으로 검색 결과 파싱
    String jsonStr = "";

    try {

      jsonStr = Jsoup.connect(String.format(SEARCH_URL,
              URLEncoder.encode(keyword, "UTF-8"), minPrice, maxPrice))
          .userAgent("Mozilla")
          .ignoreContentType(true)
          .execute().body();

    } catch (IOException e) {

      log.warn(e.getMessage());
      return mallProducts;

    }

    try {

      JsonObject data = JsonParser.parseString(jsonStr).getAsJsonObject()
          .get("data").getAsJsonObject();
      JsonArray deals = data.get("deals").getAsJsonArray();

      if (Objects.isNull(deals) || deals.size() == 0) {
        return mallProducts;
      }

      for (JsonElement deal : deals) {

        JsonObject dealObj = deal.getAsJsonObject();

        String name          = dealObj.get("dispNm").getAsString();
        String imageUrl      = dealObj.get("originImgUrl").getAsString();
        int mallPrice        = dealObj.get("discountPrice").isJsonNull() == true?
            dealObj.get("salePrice").getAsInt() : dealObj.get("discountPrice").getAsInt();
        String productMallId = dealObj.get("link").getAsJsonObject()
            .get("value").getAsString();
        String detailUrl     = String.format("https://front.wemakeprice.com/deal/%s",
            productMallId);

        // 3. MallEntity로 데이터 패키징
        mallProducts.add(MallProductEntity.builder()
            .name(name)
            .imageUrl(imageUrl)
            .detailUrl(detailUrl)
            .price(mallPrice)
            .productId(product.getProductId())
            .productMallId(productMallId)
            .build());
        
      }

    } catch(UnsupportedOperationException e) {

      log.warn(e.getMessage());
      e.printStackTrace();
      return mallProducts;

    }

    // 4. 저렴한 가격순으로 정렬하여 반환
    return mallProducts.stream()
        .sorted(Comparator.comparingInt(MallProductEntity::getPrice))
        .collect(Collectors.toList());
  }
}
