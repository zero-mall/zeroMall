package com.teamzero.product.client;

import com.teamzero.product.domain.dto.product.NaverSearchDto.Request;
import com.teamzero.product.domain.dto.product.ProductSearchDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverSearchClientTest {

  @Autowired
  private NaverSearchClient naverSearchClient;

  @Test
  @DisplayName("네이버 쇼핑 api 검색")
  void searchProducts() {

    // given
    Request naverSearch = Request.of(new ProductSearchDto.Request("돼지고기", 1, 10));

    // when
    String jsonBody = naverSearchClient.searchProducts(naverSearch).getBody();

    // then
    System.out.println(jsonBody);

  }



}