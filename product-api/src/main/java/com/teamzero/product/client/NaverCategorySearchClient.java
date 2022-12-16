package com.teamzero.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "shpping.naver", url = "https://search.shopping.naver.com/search/category")
public interface NaverCategorySearchClient {

  @GetMapping("/{categoryId}")
  ResponseEntity<String> getProducts(String categoryId);

}
