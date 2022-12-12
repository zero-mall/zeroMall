package com.teamzero.product.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.model.dto.NaverSearch;
import com.teamzero.product.domain.model.dto.ProductInfo;
import com.teamzero.product.domain.model.dto.ProductSet;
import com.teamzero.product.domain.model.repository.ProductRepository;
import com.teamzero.product.util.RedisCrud;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final NaverSearchClient naverSearchClient;

  private final ProductRepository productRepository;

  private final Gson gson;

  private final RedisCrud redisCrud;

  /**
   * 네이버 상품 검색
   * 로직 : https://app.diagrams.net/#G1yvWXyTuj0y-hajlKxKHRaxcWwVjEmoWF
   * 매개변수  : 검색어, 페이징 정보, 정렬 
   * 응답값   : 유사 명칭의 상품 목록
   */
  @Cacheable(key = "#searchQuery", value = CacheKey.NAVER_SEARCH)
  public List<ProductInfo> searchProductsFromNaver(String searchQuery, Pageable pageable) {

    // 1. 네이버 상품 검색 (페이지 지정, 정확도에 따라 결과 응답)
    NaverSearch naverSearch = NaverSearch.getInstance(searchQuery, pageable);

    // 2. 검색 결과 파싱
    List<ProductEntity> productList = getProductList(naverSearchClient.searchProducts(naverSearch).getBody());
    ProductEntity firstProduct = productList.get(0);

    // 3. 레디스의 상품 집합에 첫번째 상품을 추가 (중복 허용X)
    Optional<ProductSet> optionalProductSet = redisCrud.getData(CacheKey.NAVER_PRODUCT, ProductSet.class);
    ProductSet productSet = null;

    if (optionalProductSet.isPresent()) {
      productSet = optionalProductSet.get();
    } else {
      productSet = new ProductSet();
    }

    productSet.addProduct(firstProduct);
    redisCrud.saveData(CacheKey.NAVER_PRODUCT, productSet);

    // 4. 해당 상품이 DB에 없으면 상품 정보를 저장 후 타 쇼핑몰 정보 저장
    if (!productRepository.existsByProductId(firstProduct.getProductId())) {

      productRepository.save(firstProduct);

      // TODO 타 쇼핑몰 상세정보 저장

    }

    return productList.stream().map(ProductInfo::fromEntity).collect(Collectors.toList());
  }

  public List<ProductEntity> getProductList(String jsonBody) {

    JsonObject main = gson.fromJson(jsonBody, JsonObject.class);
    JsonArray products = main.get("items").getAsJsonArray();

    return products.asList().stream().map(e -> gson.fromJson(e.toString(), ProductEntity.class))
        .collect(Collectors.toList());
  }

}
