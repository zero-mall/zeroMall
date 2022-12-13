package com.teamzero.product.controller;

import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.model.dto.NaverProduct;
import com.teamzero.product.service.ProductService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
   * 네이버 상품 검색
   * 로직 : https://app.diagrams.net/#G1yvWXyTuj0y-hajlKxKHRaxcWwVjEmoWF
   * 매개변수  : 검색어, 페이징 정보, 정렬
   * 응답값   : 유사 명칭의 상품 목록
   */
  @GetMapping("/search/all")
  @Cacheable(key = "#search", value = CacheKey.NAVER_SEARCH)
  public ResponseEntity<?> searchProductsFromNaver(@RequestParam String search,
      @RequestParam int pageNumber, @RequestParam int pageSize) {

    // 1. 네이버 상품 검색 (페이지 지정, 검색어 정확도에 따라 결과 응답)
    List<NaverProduct> result = productService.searchNaverProducts(search, PageRequest.of(pageNumber, pageSize));

    // 상품 결과가 하나도 없거나 응답 오류가 있는 경우
    if (CollectionUtils.isEmpty(result)){
      throw new TeamZeroException(ErrorCode.PRODUCT_DATA_NOT_FOUND);
    }

    // 2. 첫번째 상품을 선택
    NaverProduct firstProduct = result.get(0);

    // 3. 캐시(Redis)에 상품이 있는지 확인
    ProductEntity product = productService.getProductFromRedis(firstProduct.getNaverId());

    // 4. DB에 상품이 있는지 확인
    if (Objects.isNull(product)) {
      // DB에 상품이 있으면 가져오고, 없을 경우 신규 저장 (네이버 정보만 저장)
      product = productService.getOrCreateProduct(ProductEntity.of(firstProduct));
      // 각 쇼핑몰에서 상품명과 유사한 상품 검색하여 저장
      productService.addMallProducts(product.getNaverId());
    }

    // 5. 캐시(Redis)에 상품 저장
    productService.addProductToRedisSet(product);

    return ResponseEntity.ok(result);
  }

}
