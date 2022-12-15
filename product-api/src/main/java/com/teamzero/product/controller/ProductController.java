package com.teamzero.product.controller;

import com.teamzero.product.domain.dto.NaverSearch;
import com.teamzero.product.domain.dto.NaverSearch.Response.NaverProduct;
import com.teamzero.product.domain.dto.ProductSearch;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.service.ProductService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  /**
   * 네이버 상품 검색 & 등록
   * 로직 : https://app.diagrams.net/#G1yvWXyTuj0y-hajlKxKHRaxcWwVjEmoWF
   * 매개변수  : 검색어, 페이징 정보, 정렬
   * 응답값   : 유사 명칭의 상품 목록
   */
  @GetMapping("/search/all")
  @Cacheable(key = "#request.keyword", value = CacheKey.NAVER_SEARCH)
  public ResponseEntity<ProductSearch.Response> searchNaverProducts(ProductSearch.Request request) {

    // 1. 네이버 쇼핑 API에서 상품 검색
    NaverSearch.Response response = productService.searchNaverProducts(request);

    // 2. 정확도가 가장 높은 상품을 DB, 캐시(Redis)에 저장
    if (!CollectionUtils.isEmpty(response.getContent())){

      // 정확도 가장 높은 첫번째 상품 선택
      NaverProduct firstProduct = response.getContent().get(0);

      // 캐시(Redis)에 해당 상품이 있는지 확인
      ProductEntity product = productService.getProductFromRedis(firstProduct.getNaverId());

      // DB에 해당 상품이 있는지 확인
      if (Objects.isNull(product)) {
        // DB에 상품이 있으면 가져오고, 없을 경우 신규 저장
        product = productService.getOrCreateProduct(ProductEntity.of(firstProduct));
        // 각 쇼핑몰에서 상품명과 유사한 상품 검색하여 저장
        productService.addMallProducts(product.getNaverId());
      }

      // 캐시(Redis)에 상품 저장
      productService.addProductToRedisSet(product);

    }

    return ResponseEntity.ok(ProductSearch.Response.of(response));
  }

}
