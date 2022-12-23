package com.teamzero.product.domain.dto.product;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.util.StringUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 네이버 상품 조회 api document
 * https://developers.naver.com/docs/serviceapi/search/shopping/shopping.md
 */
public class NaverSearch{

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    // 검색어 (UTF-8)
    private String query;

    // 페이징 관련
    private Integer display; // 페이지 내 갯수
    private Integer start;   // 페이지 시작 지점

    // 검색 결과 정렬
    private String sort;

    public static Request of(ProductSearch.Request request){

      return Request.builder()
          .query(request.getKeyword())
          .display(request.getPageSize())
          .start(request.getPageNumber())
          .sort("sim")
          .build();
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Slf4j
  public static class Response {

    private int pageNumber;
    private int pageSize;
    private long total;
    private List<NaverProduct> content;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NaverProduct{

      // 네이버 상품 정보
      // 카테고리
      private String category1; // 대분류
      private String category2; // 중분류
      private String category3; // 소분류

      // 기본 정보
      private String naverId;
      private String title;
      private String brand;
      private String imageUrl;
      private int lPrice;

    }

    /**
     * Json 파싱 & 상품명 문자열 변환
     */
    public static NaverSearch.Response parseJson(String jsonBody) {

      try {
        JsonObject main = (JsonObject) JsonParser.parseString(jsonBody);
        JsonArray jsonArray = main.get("items").getAsJsonArray();

        List<NaverProduct> naverProducts = jsonArray.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(item -> NaverProduct.builder()
                .category1(item.get("category1").getAsString())
                .category2(item.get("category2").getAsString())
                .category3(item.get("category3").getAsString())
                .naverId(item.get("productId").getAsString())
                .title(changeProductTitle(item.get("title").getAsString()))
                .brand(item.get("brand").getAsString())
                .imageUrl(item.get("image").getAsString())
                .lPrice(Integer.parseInt(item.get("lprice").getAsString()))
                .build())
            .collect(Collectors.toList());

        return Response.builder()
            .pageNumber(main.get("start").getAsInt())
            .pageSize(main.get("display").getAsInt())
            .total(main.get("total").getAsLong())
            .content(naverProducts)
            .build();

      } catch(Exception e) {

        log.error(e.getMessage());

        return null;

      }
    }

    /**
     * 상품명 문자열 변환
     * : 대괄호, '<b>', '</b>'제거, 중복 문자열 제거
     * : (22.12.10) "2Pac 투팍 - All Eyez on Me" 와 같이 '-' 있는 경우 제거
     */
    public static String changeProductTitle(String originalTitle){

      Set<String> set = new HashSet<>();
      StringBuilder sb = new StringBuilder();

      for (String s : originalTitle.split("[\\[\\]]")) {

        if (StringUtils.isNullOrEmpty(s)) continue;

        String newS = s.replaceAll("[(\\<b\\>)(\\</b\\>)]", "");

        if (set.add(newS)) {
          sb.append(newS);
        }
      }
      return sb.toString().replaceAll("-", "");
    }

  }
}
