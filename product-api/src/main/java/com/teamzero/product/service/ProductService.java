package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.PRODUCT_REDIS_SAVE_FAIL;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.client.RedisClient;
import com.teamzero.product.domain.dto.category.CategoryRegisterDto;
import com.teamzero.product.domain.dto.product.NaverSearchDto.Request;
import com.teamzero.product.domain.dto.product.NaverSearchDto.Response;
import com.teamzero.product.domain.dto.product.ProductDetail;
import com.teamzero.product.domain.dto.product.ProductSearch;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.ErrorCode;
import com.teamzero.product.exception.TeamZeroException;
import com.teamzero.product.redis.RedisNaverSearch;
import com.teamzero.product.redis.RedisProducts;
import java.time.LocalDate;
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

      return null;

    }
  }

  /**
   * 상품 간략 정보 조회
   * - 관련 정책 : 접속자가 네이버 상품 검색 결과 목록에서 상품을 선택할 때,
   *             한 번도 조회되지 않았던 상품이면 상품의 간략정보를 DB와 레디스에 저장하고,
   *             한 번 이상 조회된 상품이면 레디스에서 상품의 간략정보를 전달한다.
   */
  public ProductDetail.Response getProductShort(ProductDetail.Request request) {

    ProductDetail.Response response;

    // 1. 레디스에서 오늘자 상품 데이터 조회
    RedisProducts redisProducts = findProductBucketFromRedis(LocalDate.now());

    // 레디스에 상품이 있으면
    if (Objects.nonNull(redisProducts) &&
        redisProducts.getProductMap().containsKey(request.getNaverId())) {

      // 해당 상품 정보 반환
      response = redisProducts.getProductMap().get(request.getNaverId());

    }
    // 레디스에 상품이 없으면
    else {

      // 2. DB에서 상품을 조회
      Optional<ProductEntity> optionalProduct
          = productRepository.findByNaverId(request.getNaverId());

      // DB에 상품이 있으면
      if (optionalProduct.isPresent()) {
        // 해당 상품 정보 반환
        response = ProductDetail.Response.fromEntity(optionalProduct.get());
      }
      // DB에 상품이 없으면
      else {

        // 카테고리 생성
        String cTypeCatId = registerCategories(request);

        // DB에 상품 간략 정보를 저장
        response = ProductDetail.Response
            .fromEntity(productRepository.save(ProductEntity.from(request, cTypeCatId)));
      }

    }

    // 3. 조회수 1 증가 & 레디스에 저장
    response = viewService.increaseView(response.getProductId());

    if (Objects.nonNull(response)) {
      saveProductToRedis(redisProducts, response);
    }

    return response;
  }

  /**
   * 전체 상품 갯수 조회
   */
  public long countAllProduct() {
    return productRepository.count();
  }

  /**
   * 레디스에서 오늘자 상품 맵 조회
   */
  private RedisProducts findProductBucketFromRedis(LocalDate today){

    Optional<RedisProducts> optionalRedisProducts
        = redisClient.getData(today, RedisProducts.class);

    if (optionalRedisProducts.isEmpty()) {
      return null;
    }

    return optionalRedisProducts.get();

  }

  /**
   * 레디스에 상품 저장
   */
  private void saveProductToRedis(RedisProducts redisProducts,
      ProductDetail.Response response){

    if (Objects.isNull(redisProducts)) {
      redisProducts = new RedisProducts();
    }

    redisProducts.getProductMap().put(response.getNaverId(), response);

    boolean result = redisClient.addData(LocalDate.now(), redisProducts);

    if (!result) {
      throw new TeamZeroException(PRODUCT_REDIS_SAVE_FAIL);
    }

  }


  /**
   * 카테고리 조회 후 저장
   * - 속한 분류에 카테고리명이 없는 경우, 신규 저장
   */
  public String registerCategories(ProductDetail.Request request) {

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
      throw new TeamZeroException(ErrorCode.CATEGORY_PARAMETER_ERROR);
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
