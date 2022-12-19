package com.teamzero.product.client;

import com.teamzero.product.domain.dto.product.NaverSearch.Request;
import com.teamzero.product.domain.dto.product.ProductSearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverSearchClientTest {

  @Autowired
  private NaverSearchClient naverSearchClient;

  @Test
  void searchProducts() {

    // given
    Request naverSearch = Request.of(new ProductSearch.Request("돼지고기", 1, 10));

    // when
    String jsonBody = naverSearchClient.searchProducts(naverSearch).getBody();

    // then
    System.out.println(jsonBody);

  }



}