package com.teamzero.product.scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

@Slf4j
public class TmonScraper extends ScrapConfig implements ProductScraperInterface{

  private static final String SEARCH_URL
      = "https://search.tmon.co.kr/api/search/v4/deals?_=1670946124923"
      + "&keyword=%s&page=1&size=10&minPrice=%d&maxPrice=%d";

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
      JsonArray searchDeals = data.get("searchDeals").getAsJsonArray();

      if (Objects.isNull(searchDeals) || searchDeals.size() == 0) {
        return mallProducts;
      }

      for (JsonElement ele : searchDeals) {

        JsonObject searchDealResponse = ele.getAsJsonObject().get("searchDealResponse")
            .getAsJsonObject();
        JsonObject extraDealInfo = ele.getAsJsonObject().get("extraDealInfo")
            .getAsJsonObject();
        JsonObject dealInfo = searchDealResponse.getAsJsonObject().get("dealInfo")
            .getAsJsonObject();
        JsonObject searchInfo = searchDealResponse.getAsJsonObject().get("searchInfo")
            .getAsJsonObject();

        String name          = dealInfo.get("titleName").getAsString();
        String imageUrl      = dealInfo.get("imageInfo").getAsJsonObject()
            .get("mobile3ColImageUrl").getAsString();
        int    mallPrice     = dealInfo.get("priceInfo").getAsJsonObject()
            .get("price").getAsInt();
        String detailUrl     = extraDealInfo.get("detailUrl").getAsString();
        String productMallId = searchInfo.get("id").getAsString();

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
      return mallProducts;

    }

    return mallProducts;
  }

  @Override
  public String getMallName() {
    return "tmon";
  }

}
