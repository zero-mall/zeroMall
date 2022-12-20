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
class WeMakeShopProductScraperTest {

  public static final String WMP_URL = "https://search.wemakeprice.com/api/wmpsearch/api/v3.0/wmp-search/search.json"
      + "?searchType=DEFAULT&search_cate=top&keyword=%s&isRec=1&_service=5&_type=3&price=%d~%d";

  private static final double TOLERANCE  = 0.05;

  @Test
  void searchProducts() throws IOException {

    // given
    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    int price = 489_000;
    int minPrice = (int) Math.ceil(price - (TOLERANCE * price));
    int maxPrice = (int) Math.ceil(price + (TOLERANCE * price));

    // when
    String jsonStr = Jsoup.connect(String.format(WMP_URL,
            URLEncoder.encode(keyword, "UTF-8"), minPrice, maxPrice))
        .userAgent("Mozilla")
        .ignoreContentType(true)
        .execute().body();

    // then
    JsonObject data = JsonParser.parseString(jsonStr).getAsJsonObject()
        .get("data").getAsJsonObject();
    JsonArray deals = data.get("deals").getAsJsonArray();

    for (JsonElement deal : deals) {

      JsonObject dealObj = deal.getAsJsonObject();

      String name  = dealObj.get("dispNm").getAsString();
      String imageUrl = dealObj.get("originImgUrl").getAsString();
      int mallPrice = dealObj.get("discountPrice").isJsonNull() == true?
          dealObj.get("salePrice").getAsInt() : dealObj.get("discountPrice").getAsInt();
      String productMallId = dealObj.get("link").getAsJsonObject().get("value").getAsString();
      String detailUrl = String.format("https://front.wemakeprice.com/deal/%s", productMallId);

      System.out.printf("name : %s, imageUrl : %s, detailUrl : %s, "
              + "mallPrice: %d, productMallId : %s\n",
          name, imageUrl, detailUrl, mallPrice, productMallId);

    }

  }
}