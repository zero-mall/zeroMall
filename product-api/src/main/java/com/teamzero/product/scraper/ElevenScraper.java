package com.teamzero.product.scraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.dto.mall.ElevenShopProductDto;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElevenScraper extends ScrapConfig
    implements ProductScraperInterface {
  static private final String SEARCH_URL =
      "https://search.11st.co.kr/Search.tmall?kwd=%s";

  @Override
  public List<MallProductEntity> getScrapProductList
      (ProductEntity product) {
    long price = product.getPrice();
    long maxPrice = (long) (price + Math.floor(price * super.TOLERANCE));
    long minPrice = (long) (price - Math.floor(price * super.TOLERANCE));

    List<MallProductEntity> mallProductEntities = new ArrayList<>();

    //Jsoup을 이용해서 11번가 조회 후 일반상품에 있는 데이터 가져오기
    String encodeParam =  URLEncoder.encode
        (product.getProductName(), StandardCharsets.UTF_8);

    Connection connection =
        Jsoup.connect(String.format(SEARCH_URL,encodeParam));
    try {
      Document document = connection.get();
      Elements parsingDivs =
          document.getElementsByAttributeValue
              ("class","l_search_content");
      //데이터가 없으면 null 반환
      if(parsingDivs.size() < 1){
        return null;
      }
      Element child = parsingDivs.get(0);
      String[] strs = child.toString().split(";");
      String result ="";
      for(var item : strs){
        item = item.trim();
        if(item.startsWith("window.searchDataFactory.commonPrdList")) {
          result = item.replace
              ("window.searchDataFactory.commonPrdList = ",
                  "");
          break;
        }
      }

      //가져온 문자열 데이터 작업하기 쉽게 JSON형식으로 변환
      JsonParser parser = new JsonParser();
      JsonElement mainElement = parser.parse(result);

      JsonObject mainObject = mainElement.getAsJsonObject();
      JsonArray itemArray = mainObject.get("items").getAsJsonArray();

      JsonObject getLastObject;
      Gson gson = new GsonBuilder().create();
      for(JsonElement items : itemArray){
        getLastObject = items.getAsJsonObject();

        String imgUrl = getLastObject.get("imageUrl").getAsString();
        JsonElement ele = parser.parse
            (getLastObject.get("logDataBody").getAsString());

        //Json데이터를 가져오기위한 Dto
        ElevenShopProductDto elevenShopProductDto
            = gson.fromJson(ele,ElevenShopProductDto.class);
        long currentPrice =
            Long.parseLong(elevenShopProductDto.getLast_discount_price());
        //금액 비교하여 허용범위에 있으면 데이터 처리
        if( currentPrice >= minPrice && currentPrice <= maxPrice){
          elevenShopProductDto.setProductId(product.getProductId());
          elevenShopProductDto.setImageUrl(imgUrl);
          //elevenShopProductDto.setMallName(getMallName());
          MallProductEntity mallProductEntity
              = MallProductEntity.from(elevenShopProductDto);
          mallProductEntities.add(mallProductEntity);
          if(mallProductEntities.size() == 10){
            break;
          }
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mallProductEntities;
  }

  @Override
  public String getMallName() {
    return "11st";
  }

}
