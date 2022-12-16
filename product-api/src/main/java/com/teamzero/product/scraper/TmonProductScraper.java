package com.teamzero.product.scraper;

import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TmonProductScraper implements ProductScraperInterface {

  private static final String SEARCH_URL = "https://search.tmon.co.kr/api/search/v4/deals?_=1670946124923"
      + "&keyword=%s&page=1&size=10&minPrice=%d&maxPrice=%d";


  @Override
  public List<MallProductEntity> getScrapProductList(ProductEntity product) throws IOException {

    // 1. 기준 상품에서 검색할 정보 가져오기
    String keyword = product.getProductName();
    int price = product.getPrice();
    int minPrice = (int) Math.ceil(price - (TOLERANCE * price));
    int maxPrice = (int) Math.ceil(price + (TOLERANCE * price));

    // 2. Jsoup으로 검색 결과 파싱
    String jsonStr = Jsoup.connect(String.format(SEARCH_URL, URLEncoder.encode(keyword, "UTF-8"), minPrice, maxPrice))
        .userAgent("Mozilla")
        .ignoreContentType(true)
        .execute().body();

    // 낮은 가격순으로 저장
    PriorityQueue<int[]> tmpQueue = new PriorityQueue<>((x, y) -> x[0] - y[0]);

    JsonObject data = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject();
    JsonArray searchDeals = data.get("searchDeals").getAsJsonArray();

    for (JsonElement ele : searchDeals) {

      JsonObject searchDealResponse = ele.getAsJsonObject().get("searchDealResponse").getAsJsonObject();
      JsonObject dealInfo = searchDealResponse.getAsJsonObject().get("dealInfo").getAsJsonObject();

      String titleName = dealInfo.get("titleName").getAsString();
      String priceInfo = dealInfo.get("priceInfo").getAsJsonObject().get("price").getAsString();

      System.out.println("titleName : " + titleName + ", priceInfo : " + priceInfo);

    }

    return null;
  }
}
