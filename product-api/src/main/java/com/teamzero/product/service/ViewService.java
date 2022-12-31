package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.CATEGORY_SEARCH_BAD_REQEUST;
import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;
import com.teamzero.product.domain.dto.ViewDto;
import com.teamzero.product.domain.dto.product.ProductDetailDto;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.CategoryRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ViewService {

  private final ProductRepository productRepository;

  private final CategoryRepository categoryRepository;

  /**
   * 상품의 조회수 +1 증가
   */
  @Transactional
  public ProductDetailDto.Response increaseView(Long productId) {

    // 상품 id 로 상품 조회
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new TeamZeroException(PRODUCT_NOT_FOUND));

    // 상품의 조회수 + 1 증가
    product.setViewCount(product.getViewCount() + 1);

    // 상품 Dto로 반환
    return ProductDetailDto.Response.fromEntity(product);
  }

  /**
   * 상품의 조회수 조회
   */
  public ViewDto getProductViewCount(Long productId) {

    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new TeamZeroException(PRODUCT_NOT_FOUND));

    return ViewDto.builder()
        .productId(productId)
        .productName(product.getProductName())
        .productViewCount(product.getViewCount())
        .build();
  }

  /**
   * 카테고리에 속한 상품들의 총 조회수와 평균 조회수 반환
   */
  public ViewDto getCatViewCountAndAvgView(String catId) {

    // 1. 현재 카테고리부터 하위 카테고리들을 모두 조회
    List<CategoryEntity> categories
        = categoryRepository.findAllByCatIdLike(catId);

    if (CollectionUtils.isEmpty(categories)) {
      throw new TeamZeroException(CATEGORY_SEARCH_BAD_REQEUST);
    }

    // 2. 조회된 카테고리들에 속한 상품들을 모두 조회
    List<ProductEntity> products = new ArrayList();

    for (CategoryEntity cat : categories) {

      productRepository.findAllByCatId(cat.getCatId()).stream()
          .filter(p -> !products.contains(p)).forEach(products::add);

    }

    // 3. 각 상품들의 총 조회수를 합산
    long catViewCount
        = products.stream().mapToLong(e -> e.getViewCount()).sum();

    // 4. 카테고리에 속한 상품들의 총 조회수와 평균 조회수 반환
    return ViewDto.builder()
        .catId(catId)
        .catViewCount(catViewCount)
        .catViewAvg(Math.round((catViewCount * 10) / products.size() / 10.0))
        .build();

  }

}
