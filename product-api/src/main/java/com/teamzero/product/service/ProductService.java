package com.teamzero.product.service;

import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.client.RedisClient;
import com.teamzero.product.domain.dto.product.NaverSearch.Request;
import com.teamzero.product.domain.dto.product.NaverSearch.Response;
import com.teamzero.product.domain.dto.product.ProductDetail;
import com.teamzero.product.domain.dto.product.ProductSearch;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.dto.category.CategoryRegister;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.redis.RedisNaverSearch;
import com.teamzero.product.redis.RedisProducts;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

  private final CategoryService categoryService;

  private final RedisClient redisClient;

  /**
   * 네이버 상품 검색
   */
  public ProductSearch.Response searchNaverProducts(ProductSearch.Request request) {

    try {

      ProductSearch.Response response;
      
      // 레디스에 검색 결과가 저장되어 있는지 확인
      var redisNaverSearchOptional
          = redisClient.getData(request.getKeyword(), RedisNaverSearch.class);

      if (redisNaverSearchOptional.isPresent()) {

        // 1) 있으면 캐시 정보가 페이지 정보와 일치할 때 반환
        response = redisNaverSearchOptional.get().getResponse();

        if (request.getPageNumber() == response.getPageNumber() &&
            request.getPageSize() == response.getPageSize()) {
          return response;
        }
        
      }

      // 2) 없으면 네이버 api 통해 검색, 레디스에 저장
      // 네이버 검색
      String jsonBody = naverSearchClient.searchProducts(Request.of(request)).getBody();

      // Json 파싱 & 상품명 문자열 변환
      response = ProductSearch.Response.of(Objects.requireNonNull(Response.parseJson(jsonBody)));

      // 레디스에 저장
      redisClient.addData(request.getKeyword(), response);
      
      return response;

    } catch (Exception e) {

      log.error(e.getMessage());

      return null;

    }
  }

  /**
   * 상품 간략 정보 조회
   * - 상품이 있는 경우, 해당 상품 반환
   * - 상품이 없는 경우, DB에 저장 후 반환
   */
  @Transactional
  public ProductDetail.Response getProductShort(ProductDetail.Request request) {

    // 1. 레디스에서 상품을 조회
    Optional<RedisProducts> optionalRedisProducts = redisClient.getData(LocalDate.now(), RedisProducts.class);

    RedisProducts redisProducts;

    if (optionalRedisProducts.isPresent()) {

      redisProducts = optionalRedisProducts.get();

      // 레디스에 상품이 있으면 해당 상품 정보 반환
      if (redisProducts.getProductMap().containsKey(request.getNaverId())) {
        return redisProducts.getProductMap().get(request.getNaverId());
      }

    } else {
      redisProducts = new RedisProducts();
    }

    // 2. 레디스에 없으면 DB에서 상품을 조회
    Optional<ProductEntity> optionalProduct = productRepository.findByNaverId(request.getNaverId());

    // 3. DB에 없으면 DB에 상품을 저장하고 DTO 타입으로 변경
    ProductDetail.Response response;

    if (optionalProduct.isPresent()) {
      response = ProductDetail.Response.fromEntity(optionalProduct.get());
    } else {
      // 카테고리 생성
      String cTypeCatId = registerCategories(request);

      // DB에 저장
      response = ProductDetail.Response.fromEntity(productRepository.save(ProductEntity.from(request, cTypeCatId)));
    }

    // 4. 레디스에 저장
    redisProducts.getProductMap().put(response.getNaverId(), response);

    boolean result = redisClient.addData(LocalDate.now(), redisProducts);

    if (!result) {
      log.warn(response.getNaverId() + "데이터가 레디스에 정상적으로 저장되지 않았습니다.");
    }

    return response;
  }

  /**
   * 카테고리 조회 후 저장
   * - 속한 분류에 카테고리명이 없는 경우, 신규 저장
   */
  @Transactional
  public String registerCategories(ProductDetail.Request request) {

    String aTypeId = "";
    String bTypeId = "";

    // 대분류 조회 후 필요시 등록
    CategoryRegister aType = CategoryRegister.builder()
        .catName(request.getCategory1())
        .catType("atype")
        .build();

    aTypeId = getCatId(aType);

    // 중분류 조회 후 필요시 등록
    CategoryRegister bType = CategoryRegister.builder()
        .catName(request.getCategory2())
        .catType("btype")
        .parentCatId(aTypeId)
        .build();

    bTypeId = getCatId(bType);

    // 소분류 조회 후 필요시 등록
    CategoryRegister cType = CategoryRegister.builder()
        .catName(request.getCategory3())
        .catType("ctype")
        .parentCatId(bTypeId)
        .build();

    return getCatId(cType);

  }

  private String getCatId(CategoryRegister request) {

    List<CategoryEntity> categories = categoryService.categoryFind(request);

    Optional<CategoryEntity> cat = categories.stream()
        .filter(c -> c.getCatName().equals(request.getCatName()))
        .findFirst();

    if (cat.isPresent()) {
      return cat.get().getCatId();
    } else {
      return categoryService.categoryRegister(request).getCatId();
    }

  }

}
