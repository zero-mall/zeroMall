package com.teamzero.product.service;

import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.redis.RedisClient;
import com.teamzero.product.domain.dto.category.CategoryRegisterDto;
import com.teamzero.product.domain.dto.product.NaverSearchDto.Request;
import com.teamzero.product.domain.dto.product.NaverSearchDto.Response;
import com.teamzero.product.domain.dto.product.ProductDetailDto;
import com.teamzero.product.domain.dto.product.ProductSearchDto;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.ErrorCode;
import com.teamzero.product.exception.TeamZeroException;
import com.teamzero.product.redis.RedisNaverSearch;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final NaverSearchClient naverSearchClient;

  private final ProductRepository productRepository;

  private final CategoryService categoryService;

  private final ViewService viewService;

  private final RedisClient redisClient;

  /**
   * 네이버 상품 검색
   * - 네이버 쇼핑 검색 api 를 통해서 네이버의 상품 검색 결과를 반환
   */
  public ProductSearchDto.Response searchNaverProducts(
      ProductSearchDto.Request request) {

    try {

      ProductSearchDto.Response response;
      
      // 레디스에 검색 결과가 저장되어 있는지 확인
      Optional<RedisNaverSearch> redisNaverSearchOptional
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
      String jsonBody = naverSearchClient.searchProducts(
          Request.of(request)).getBody();

      // Json 파싱 & 상품명 문자열 변환
      response = ProductSearchDto.Response.of(
          Objects.requireNonNull(Response.parseJson(jsonBody)));

      // 레디스에 저장
      redisClient.addData(request.getKeyword(), response);
      
      return response;

    } catch (Exception e) {

      return null;

    }
  }

  /**
   * 상품 간략 정보 저장
   * - 사용자가 상품 검색 결과 목록에서 특정 상품을 선택할 때,
   *   상품에 대한 간략 정보를 전달하면 해당 정보를 저장한다.
   *   만약, 이미 DB에 저장된 상품이라면 저장 없이 해당 정보를 반환한다.
   */
  public ProductDetailDto.Response createOrJustIncreaseViewProduct(
      ProductDetailDto.Request request) {

    Optional<ProductEntity> optionalProduct
        = productRepository.findByNaverId(request.getNaverId());

    // 1. DB에 상품이 있으면 
    if (optionalProduct.isPresent()) {

      // 조회수를 1 증가 후 해당 상품 정보를 반환
      return viewService.increaseView(optionalProduct.get().getProductId());
    }

    // 2. DB에 상품이 없으면
    else {

      // (1) 카테고리 생성
      String cTypeCatId = registerCategories(request);

      // (2) DB에 상품 간략 정보를 저장
      ProductDetailDto.Response response = ProductDetailDto.Response
          .fromEntity(productRepository.save(
              ProductEntity.from(request, cTypeCatId)));

      // (3) DB의 상품 조회수 1 증가
      response = viewService.increaseView(response.getProductId());

      return response;
    }

  }

  /**
   * 카테고리 조회 후 저장
   * - 속한 분류에 카테고리명이 없는 경우, 신규 저장
   */
  public String registerCategories(ProductDetailDto.Request request) {

    String aTypeId = "";
    String bTypeId = "";

    // 대분류 조회 후 필요시 등록
    CategoryRegisterDto aType = CategoryRegisterDto.builder()
        .catName(request.getCategory1())
        .catType("atype")
        .build();

    aTypeId = getCatId(aType);

    // 중분류 조회 후 필요시 등록
    CategoryRegisterDto bType = CategoryRegisterDto.builder()
        .catName(request.getCategory2())
        .catType("btype")
        .parentCatId(aTypeId)
        .build();

    bTypeId = getCatId(bType);

    // 소분류 조회 후 필요시 등록
    CategoryRegisterDto cType = CategoryRegisterDto.builder()
        .catName(request.getCategory3())
        .catType("ctype")
        .parentCatId(bTypeId)
        .build();

    return getCatId(cType);

  }

  private String getCatId(CategoryRegisterDto request) {

    List<CategoryEntity> categories = categoryService.categoryFind(request);

    if (Objects.isNull(categories) || categories.size() == 0) {
      throw new TeamZeroException(ErrorCode.CATEGORY_SEARCH_BAD_REQEUST);
    }

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
