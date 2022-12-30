package com.teamzero.product.controller;

import com.teamzero.product.domain.dto.product.ProductDetailDto;
import com.teamzero.product.domain.dto.product.ProductSearchDto;
import com.teamzero.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  /**
   * 네이버 상품 검색
   * 로직 : https://app.diagrams.net/#G1yvWXyTuj0y-hajlKxKHRaxcWwVjEmoWF
   * 매개변수  : 검색어, 페이징 정보
   * 응답값   : 네이버 상품 검색 결과
   */
  @GetMapping("/api/search")
  public ResponseEntity<ProductSearchDto.Response> searchNaverProducts(@Valid ProductSearchDto.Request request) {

    return ResponseEntity.ok(productService.searchNaverProducts(request));
  }

  /**
   * 상품 간략 정보 조회
   * - 관련 정책 : 접속자가 네이버 상품 검색 결과 목록에서 상품을 선택할 때,
   *             한 번도 조회되지 않았던 상품이면 상품의 간략정보을 DB와 레디스에 저장하고,
   *             한 번 이상 조회된 상품이면 레디스에서 상품의 간략정보를 전달한다.
   */
  @GetMapping("/short")
  public ResponseEntity<ProductDetailDto.Response> getProductShort(ProductDetailDto.Request request) {

    return ResponseEntity.ok(productService.createOrJustIncreaseViewProduct(request));

  }

}
