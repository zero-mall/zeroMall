package com.teamzero.product.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.util.StringUtils;
import com.teamzero.member.exception.ErrorCode;
import com.teamzero.member.exception.TeamZeroException;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.model.dto.NaverProduct;
import com.teamzero.product.domain.model.dto.NaverSearch;
import com.teamzero.product.domain.model.dto.ProductSet;
import com.teamzero.product.domain.model.repository.ProductRepository;
import com.teamzero.product.util.RedisCrud;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
  public List<NaverProduct> searchNaverProducts(String searchQuery, Pageable pageable) {

    try {

      // 1. 네이버 검색 쿼리
      NaverSearch naverSearch = NaverSearch.getInstance(searchQuery, pageable);
      // 2. 네이버 검색 결과
      String jsonBody = naverSearchClient.searchProducts(naverSearch).getBody();
      // 3. Json 파싱
      return toJson(jsonBody);

    } catch (Exception e) {

      log.error(e.getMessage());
      return null;

    }
  }

  private List<NaverProduct> toJson(String jsonBody) {

    JsonObject main = (JsonObject) JsonParser.parseString(jsonBody);
    JsonArray products = main.get("items").getAsJsonArray();

    return products.asList().stream().map(e -> e.getAsJsonObject()).map(NaverProduct::parseJson)
        .collect(Collectors.toList());
  }

  /**
   * 캐시(Redis)에서 네이버 상품 조회
   */
  public ProductEntity getProductFromRedis(Long naverId) {

    Optional<ProductSet> optionalProductSet = redisCrud.getData(CacheKey.NAVER_PRODUCT, ProductSet.class);

    if (optionalProductSet.isEmpty()) {
      return null;
    }

    Optional<ProductEntity> optionalProduct = optionalProductSet.get().getProducts().stream()
        .filter(e -> e.getNaverId().equals(naverId)).findFirst();

    if (optionalProduct.isEmpty()) {
      return null;
    }

    return optionalProduct.get();

  }

  /**
   * DB에서 네이버 상품 조회 또는 저장
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

    // 2. 네이버 상품명 문자열 변환 (<b> 제거, 중복 문자열 제거)
    // ex. "<b>돼</b>염 [<b>돼</b>염] 프리미엄 저염 솔트에이징 목살 2kg"
    String newSearch = getNewSearchString(product.getName());

    // TODO 3. 상품 카테고리 id 생성

    // TODO 4. 쇼핑몰 정보 스크래핑하여 저장 (정확도 및 가격 오차범위 5%의 검색 결과 첫번째)
    List<MallProductEntity> mallProducts = new ArrayList<>();
    // TODO 찬혁 (각각 함수를 별도로 작성하면 더 좋을 것 같아요.)
    // TODO 고은
    // TODO 지수

    // 5. 쇼핑몰 정보 업데이트
    product.setMallProducts(mallProducts);

  }

  /**
   * 상품명 문자열 변환
   * : 대괄호, '<b>', '</b>'제거, 중복 문자열 제거
   * : ex. "<b>돼</b>염 [<b>돼</b>염] 프리미엄 저염 솔트에이징 목살 2kg"
   * : ex. "[<b>돼</b>염][<b>돼</b>염] 프리미엄 저염 솔트에이징 목살 1kg"
   * : ex. "<b>삼성전자 노트북</b> 플러스2 NT550XDA-K14A"
   */
  public String getNewSearchString(String title){

    Set<String> set = new HashSet<>();
    StringBuilder sb = new StringBuilder();

    for (String s : title.split("[\\[\\]]")) {

      if (StringUtils.isNullOrEmpty(s)) continue;

      String newS = s.replaceAll("[(\\<b\\>)(\\</b\\>)]", "");

      if (set.add(newS)) {
        sb.append(newS);
      }
    }

    return sb.toString();
  }

  /**
   * 캐시(Redis)에 상품 저장
   */
  public boolean addProductToRedisSet(ProductEntity product) {

    Optional<ProductSet> optionalSet = redisCrud.getData(CacheKey.NAVER_PRODUCT, ProductSet.class);
    ProductSet set;

    if (optionalSet.isPresent()) {
      set = optionalSet.get();
    } else {
      set = new ProductSet();
    }

    set.addProduct(product);

    return redisCrud.saveData(CacheKey.NAVER_PRODUCT, set);
  }

}
