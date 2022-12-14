package com.teamzero.product.client;

import com.teamzero.product.domain.dto.TmonSearch;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class TmonSearchClientTest {

  @Autowired
  private TmonSearchClient tmonSearchClient;

  @Test
  void searchProducts() {

    // given
    String keyword = "삼성전자 노트북 플러스2 NT550XDA-K14A";
    String commonFilter = "minPrice:450000,maxPrice:500000";
    TmonSearch tmonSearch = new TmonSearch(keyword, commonFilter);

    // when
    String html = tmonSearchClient.searchProducts(tmonSearch).getBody();

    // then
    try {
      OutputStream out = new FileOutputStream("D:/세빈/out.html");
      out.write(html.getBytes());
    } catch(Exception e) {
      e.printStackTrace();
    }

    System.out.println(html.contains("deallist_wrap"));

  }

  @Test
  void searchProductViaSimpleURL() {

    String uri = "https://search.tmon.co.kr/search/?keyword=삼성전자 노트북 플러스2 NT550XDA-K14A&commonFilters=minPrice:450000,maxPrice:5000000&thr=hs";

    try {

      URL url = new URL(uri);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();

      System.out.println(conn.getResponseCode());

      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains("deallist_wrap")) {
          System.out.println("OK!");
        }
      }
      System.out.println("NO!");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}