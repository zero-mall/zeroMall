package com.teamzero.product.controller;

import com.teamzero.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  /**
   * 네이버 상품 조회 - 네이버의 상품 검색 api 사용 - 매개변수 : 검색어, 페이징 정보, 정렬
   */
  @GetMapping("/search/all")
  public ResponseEntity<?> searchProductsFromNaver(@RequestParam String search,
      @RequestParam int pageNumber, @RequestParam int pageSize) {

    productService.searchProductsFromNaver(search, PageRequest.of(pageNumber, pageSize));

    return null;
  }

}
