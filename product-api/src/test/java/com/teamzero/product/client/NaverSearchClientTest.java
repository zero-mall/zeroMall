package com.teamzero.product.client;

import com.teamzero.product.domain.dto.NaverSearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class NaverSearchClientTest {

  @Autowired
  private NaverSearchClient naverSearchClient;

  @Test
  void searchProducts() {

    // given
    String search = "돼지고기";
    Pageable pageable = PageRequest.of(0, 10);
    NaverSearch naverSearch = NaverSearch.getInstance(search, pageable);

    // when
    String jsonBody = naverSearchClient.searchProducts(naverSearch).getBody();

    // then
    System.out.println(jsonBody);

  }



}