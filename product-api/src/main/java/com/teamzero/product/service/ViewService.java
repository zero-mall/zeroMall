package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.CATEGORY_PARAMETER_ERROR;
import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.teamzero.product.domain.dto.View;
import com.teamzero.product.domain.dto.product.ProductDetail;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.CategoryRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
   * 특정 상품의 조회수 +1 증가
   */
  @Transactional
  public ProductDetail.Response increaseView(Long productId) {

    ProductEntity product = productRepository.findById(productId).orElse(null);

    if (Objects.isNull(product)) return null;

    product.setViewCount(product.getViewCount() + 1);

    return ProductDetail.Response.fromEntity(product);
  }

  /**
   * 특정 상품의 조회수 조회
   */
  public View.Response.ProductView getProductViewResponse(Long productId) {

    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new TeamZeroException(PRODUCT_NOT_FOUND));

    return new View.Response.ProductView(productId, product.getProductName(),
        product.getViewCount());
  }

  /**
   * 특정 카테고리의 평균 조회수 조회
   */
  public View.Response.CatView getCatViewResponse(String catId){

    List<CategoryEntity> categories = categoryRepository.findAllByCatIdLike(catId);

    if (CollectionUtils.isEmpty(categories)) {
      throw new TeamZeroException(CATEGORY_PARAMETER_ERROR);
    }

    List<ProductEntity> products = categories.stream()
        .map(c -> productRepository.findByCatId(c.getCatId()).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    double avgCatView = products.stream()
        .mapToInt(p -> (int) p.getViewCount()).average().orElse(-1.0);

    return new View.Response.CatView(catId, products.size(), avgCatView);

  }

  /**
   * 전체 상품들의 평균 조회수 조회
   */
  public double getTotalAvgProductView() {

    return productRepository.getTotalAvgViewCount();
  }

}
