package com.teamzero.product.client;

import com.teamzero.product.domain.dto.NaverSearch.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "naver", url = "https://openapi.naver.com/v1/search/shop.json")
@Qualifier("naver_search")
public interface NaverSearchClient {

  @GetMapping
  ResponseEntity<String> searchProducts(@SpringQueryMap Request naverSearch);

}
