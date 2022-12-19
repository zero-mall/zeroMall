package com.teamzero.product.scraper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GMarketRedisProductsScraperTest {

    private static final String GMARKET_URL = "https://browse.gmarket.co.kr/search?keyword=";
    private static final String AKMALL_URL = "https://www.akmall.com/search/Search2.do?search=";


    private static final double TOLERANCE = 0.05;


    @Test
    @DisplayName("Gmarket 스크래핑 테스트")
    void getScrapProductList_Gmarket() {

        String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
        int price = 489000;
        int minPrice = (int) Math.round(price - (TOLERANCE * price));
        int maxPrice = (int) Math.round(price + (TOLERANCE * price));
        // 가격 필터
        String priceAdd = "&f=p:" + minPrice + "^" + maxPrice + "&s=1";
        Document document = null;
        try {
            Connection connection = Jsoup.connect(
                GMARKET_URL + URLEncoder.encode(keyword, "UTF-8") + priceAdd);

            connection
                .header("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 상품명
        Iterator<Element> ie1 = document.select("span.text__item").iterator();

        // 상품 가격
        Iterator<Element> ie2 = document.select("div.box__price-seller strong").iterator();

        System.out.println("=====================");
        while (ie1.hasNext()) {
            System.out.println(ie1.next().text() + " : " + ie2.next().text() + "원");
        }
        System.out.println("=====================");
    }

}