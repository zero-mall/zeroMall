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
class TmonScraperTest {

  public static final String TMON_URL = "https://search.tmon.co.kr/api/search/v4/deals?_=1670946124923"
      + "&keyword=%s&page=1&size=10&minPrice=%d&maxPrice=%d";

  private static final double TOLERANCE  = 0.05;

  @Test
  void searchProducts() throws IOException {

    // given
    String keyword = "삼성전자 컴퓨터";
    int price = 489_000;
    int minPrice = (int) Math.ceil(price - (TOLERANCE * price));
    int maxPrice = (int) Math.ceil(price + (TOLERANCE * price));

    // when
    String jsonStr = Jsoup.connect(String.format(TMON_URL, URLEncoder.encode(keyword, "UTF-8"), minPrice, maxPrice))
        .userAgent("Mozilla")
        .ignoreContentType(true)
        .execute().body();

    // then
    JsonObject data = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject();
    JsonArray searchDeals = data.get("searchDeals").getAsJsonArray();

    for (JsonElement ele : searchDeals) {

      JsonObject searchDealResponse = ele.getAsJsonObject().get("searchDealResponse").getAsJsonObject();
      JsonObject dealInfo = searchDealResponse.getAsJsonObject().get("dealInfo").getAsJsonObject();

      String titleName = dealInfo.get("titleName").getAsString();
      String priceInfo = dealInfo.get("priceInfo").getAsJsonObject().get("price").getAsString();

      System.out.println("titleName : " + titleName + ", priceInfo : " + priceInfo);

    }

  }

}