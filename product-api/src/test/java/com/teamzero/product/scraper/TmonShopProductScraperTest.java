package com.teamzero.product.scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URLEncoder;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TmonShopProductScraperTest {

  public static final String TMON_URL = "https://search.tmon.co.kr/api/search/v4/deals?_=1670946124923"
      + "&keyword=%s&page=1&size=10&minPrice=%d&maxPrice=%d";

  private static final double TOLERANCE  = 0.05;

  @Test
  void searchProducts() throws IOException {

    // given
    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    int price = 489_000;
    int minPrice = (int) Math.ceil(price - (TOLERANCE * price));
    int maxPrice = (int) Math.ceil(price + (TOLERANCE * price));

    // when
    String jsonStr = Jsoup.connect(String.format(TMON_URL,
            URLEncoder.encode(keyword, "UTF-8"), minPrice, maxPrice))
        .userAgent("Mozilla")
        .ignoreContentType(true)
        .execute().body();

    // then
    JsonObject data = JsonParser.parseString(jsonStr)
        .getAsJsonObject().get("data")
        .getAsJsonObject();
    JsonArray searchDeals = data.get("searchDeals").getAsJsonArray();

    for (JsonElement ele : searchDeals) {

      JsonObject searchDealResponse = ele.getAsJsonObject()
          .get("searchDealResponse").getAsJsonObject();
      JsonObject extraDealInfo = ele.getAsJsonObject()
          .get("extraDealInfo").getAsJsonObject();
      JsonObject dealInfo = searchDealResponse.getAsJsonObject()
          .get("dealInfo").getAsJsonObject();
      JsonObject searchInfo = searchDealResponse.getAsJsonObject()
          .get("searchInfo").getAsJsonObject();

      String name  = dealInfo.get("titleName").getAsString();
      String imageUrl = dealInfo.get("imageInfo").getAsJsonObject()
          .get("mobile3ColImageUrl").getAsString();
      int mallPrice = dealInfo.get("priceInfo").getAsJsonObject().get("price").getAsInt();
      String detailUrl = extraDealInfo.get("detailUrl").getAsString();
      String productMallId = searchInfo.get("id").getAsString();

      System.out.printf("name : %s, imageUrl : %s, detailUrl : %s, "
              + "mallPrice: %d, productMallId : %s\n",
          name, imageUrl, detailUrl, mallPrice, productMallId);

    }

  }
}