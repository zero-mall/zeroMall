package com.teamzero.product.service;

import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.domain.dto.NaverSearch;
import com.teamzero.product.domain.dto.NaverSearch.Request;
import com.teamzero.product.domain.dto.ProductSearch;
import com.teamzero.product.domain.dto.RedisProductSet;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.util.RedisCrud;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final NaverSearchClient naverSearchClient;

  private final ProductRepository productRepository;

  private final RedisCrud redisCrud;

  /**
   * 네이버 상품 검색
   */
  public NaverSearch.Response searchNaverProducts(ProductSearch.Request request) {

    try {

      // 1. 네이버 검색
      String jsonBody = naverSearchClient.searchProducts(Request.of(request)).getBody();

      // 2. Json 파싱 & 상품명 문자열 변환
      return NaverSearch.Response.parseJson(jsonBody);

    } catch (Exception e) {

      log.error(e.getMessage());

      return null;

    }
  }

  /**
   * 캐시(Redis)에서 네이버 상품 조회
   */
  public ProductEntity getProductFromRedis(Long naverId) {

    // 1. 캐시에서 상품 집합 불러오기
    Optional<RedisProductSet> optionalProductSet = redisCrud.getData(CacheKey.NAVER_PRODUCT, RedisProductSet.class);

    if (optionalProductSet.isEmpty()) {
      return null;
    }

    // 2. 찾는 상품이 있는 경우 해당 상품 반환
    Optional<ProductEntity> optionalProduct = optionalProductSet.get().getProducts().stream()
        .filter(e -> e.getNaverId().equals(naverId)).findFirst();

    if (optionalProduct.isEmpty()) {
      return null;
    }

    return optionalProduct.get();

  }

  /**
   * DB에서 네이버 상품 조회 및 저장
   * - DB에 상품이 있는 경우 해당 상품 반환
   * - DB에 상품이 없는 경우 상품 저장 후 반환
   */
  public ProductEntity getOrCreateProduct(ProductEntity product) {

    Optional<ProductEntity> optionalProduct = productRepository.findByProductId(product.getProductId());

    return optionalProduct.orElseGet(() -> productRepository.save(product));

  }

  /**
   * 다른 쇼핑몰들 유사 상품 조회 & 저장
   */
  @Transactional
  public void addMallProducts(Long naverId) {

    // 1. 네이버 상품 DB 조회
    ProductEntity product = productRepository.findByNaverId(naverId)
        .orElseThrow(() -> new TeamZeroException(ErrorCode.PRODUCT_NOT_FOUND));

    // TODO 2. 상품 카테고리 id 생성

    // TODO 3. 쇼핑몰 정보 스크래핑하여 저장 (정확도 및 가격 오차범위 5%의 검색 결과 첫번째)
    List<MallProductEntity> mallProducts = new ArrayList<>();
    // TODO 찬혁 (각각 함수를 별도로 작성하면 더 좋을 것 같아요.)
    // TODO 고은
    // TODO 지수

    // 5. 쇼핑몰 정보 업데이트
    product.setMallProducts(mallProducts);

  }

  /**
   * 캐시(Redis)에 상품 저장
   */
  public boolean addProductToRedisSet(ProductEntity product) {

    Optional<RedisProductSet> optionalSet = redisCrud.getData(CacheKey.NAVER_PRODUCT, RedisProductSet.class);
    RedisProductSet set;

    if (optionalSet.isPresent()) {
      set = optionalSet.get();
    } else {
      set = new RedisProductSet();
    }

    set.addProduct(product);

    return redisCrud.saveData(CacheKey.NAVER_PRODUCT, set);
  }

}
