package com.teamzero.product.scraper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TmonProductScraperTest {
  private static final String TMON_URL = "https://search.tmon.co.kr/search/?keyword=%s&commonFilters=minPrice:%d,maxPrice:%d&thr=hs";
  private static final String CHUNSAMBAEK_URL = "https://www.1300k.com/shop/search?keyword=%s&priceStart=%d&priceEnd=%d&pageNumber=1";
  private static final String SSG_URL = "https://www.ssg.com/search.ssg?target=all&query=%s&minPrc=%d&maxPrc=%d";

  private static final double TOLERANCE  = 0.05;

  // 위메프, 티몬, NS Mall, CJ ONSTYLE, 홈&쇼핑, GS shop, 롯데홈쇼핑, 오늘의집, 인터파크.. 스크래핑 불가
  // 국내 쇼핑몰 목록 : https://www.shoplinker.co.kr/shoppingMall/shoplinker/applyMall

  @Test
  @DisplayName("LOTTE 스크래핑 테스트")
  void getScrapProductList_LOTTE(){

    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    int price = 489_000;
    int minPrice = (int) Math.round(price - (TOLERANCE * price));
    int maxPrice = (int) Math.round(price + (TOLERANCE * price));

    try {

      // Jsoup을 통해서 데이터 스크래핑
      // * 관련 정책 : 기준인 네이버 상품 가격으로부터 오차범위 5%가 나는 상품 조회
      // * 결과 : LOTTE mall 텍스트만 나타남
      Connection connection = Jsoup.connect("https://www.lotteimall.com/search/searchMain.lotte?slog=00101_1&headerQuery=" + keyword.replaceAll(" ", "+"))
          .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
          .timeout(60000);
      Document document = connection.get();

      // Elements search = document.getElementsByClass("contents");
      // Element  result = document.getElementById("search_result_goods_info_15_");
      // Elements res = document.getElementsByClass("wrap_unitlist");
      // System.out.println(document.getElementsByClass("area_search_result_list"));
      // System.out.println(document.getElementsByAttributeValue("class", "title"));
      // System.out.println(document.getElementsByTag("ul"));
      // System.out.println(document.select("wrap_unitlist"));
      // System.out.println(document.getElementsByTag("ul"));
      // System.out.println(document.getElementsContainingText("wrap_unitlist"));
      // System.out.println(document.select(".search_result_goods_info_15_"));
      // System.out.println(document.select(".wrap_unitlist ul"));
      System.out.println(document.getAllElements());

    } catch(Exception e) {
      System.out.println("=====ERROR!!=====");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

  }

  @Test
  @DisplayName("SSG 스크래핑 테스트")
  void getScrapProductList_ssg(){

    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    int price = 489_000;
    int minPrice = (int) Math.round(price - (TOLERANCE * price));
    int maxPrice = (int) Math.round(price + (TOLERANCE * price));
    String url = String.format(SSG_URL, keyword.replaceAll(" ", "%"), minPrice, maxPrice);

    try {

      // Jsoup을 통해서 데이터 스크래핑
      // * 관련 정책 : 기준인 네이버 상품 가격으로부터 오차범위 5%가 나는 상품 조회
      // * 결과 : 아무 결과도 나타나지 않음
      Connection connection = Jsoup.connect(url);
      connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
      connection.timeout(600000);
      Document document = connection.get(); // GET 방식

      System.out.println(document.text());

    } catch(Exception e) {
      System.out.println("=====ERROR!!=====");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

  }

  @Test
  @DisplayName("1300k 스크래핑 테스트")
  void getScrapProductList_1300k(){

    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    int price = 489_000;
    int minPrice = (int) Math.round(price - (TOLERANCE * price));
    int maxPrice = (int) Math.round(price + (TOLERANCE * price));
    String url = String.format(CHUNSAMBAEK_URL, keyword.replaceAll(" ", "%"), minPrice, maxPrice);

    try {

      // Jsoup을 통해서 데이터 스크래핑
      // * 관련 정책 : 기준인 네이버 상품 가격으로부터 오차범위 5%가 나는 상품 조회
      // * 결과 : We're sorry but Webpack App doesn't work properly without JavaScript enabled. Please enable it to continue.
      Connection connection = Jsoup.connect(url);
      connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
      Document document = connection.get(); // GET 방식

      System.out.println(document.text());

    } catch(Exception e) {
      System.out.println("=====ERROR!!=====");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

  }

  @Test
  @DisplayName("티몬 스크래핑 테스트")
  void getScrapProductList_Tmon() {

    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A".replaceAll(" ", "+");
    int price = 489_000;
    int minPrice = (int) Math.round(price - (TOLERANCE * price));
    int maxPrice = (int) Math.round(price + (TOLERANCE * price));
    String url = String.format(TMON_URL, keyword, minPrice, maxPrice);

    try {
      
      // Jsoup을 통해서 데이터 스크래핑
      // * 관련 정책 : 기준인 네이버 상품 가격으로부터 오차범위 5%가 나는 상품 조회
      // * 결과 : 502 오류가 지속적으로 나타남 (GET, POST 모두 안됨)
      Connection connection = Jsoup.connect(url);
      connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
      connection.timeout(30000);
      Document document = connection.get(); // GET 방식

      Element  section = document.getElementsByClass("search_deallist").get(0);
      Element  divWrap = section.getElementsByClass("deallist_wrap").get(0);
      Element  ulDatas = divWrap.getElementsByClass("list").get(0);

      for (Element e : ulDatas.children()) {

        String aTag = e.child(0).text();

        System.out.println(aTag);

      }
      
    } catch(Exception e) {
      System.out.println("=====ERROR!!=====");
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

  }
}