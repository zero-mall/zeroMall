package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.CategoryRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ViewService viewService;

  @Test
  @DisplayName("상품의 조회수 +1 증가")
  void increaseView() {

    // given
    given(productRepository.findById(anyLong()))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(1L)
            .catId("123456789")
            .productName("test")
            .viewCount(123)
            .build()));

    // when
    var result = viewService.increaseView(1L);

    // then
    Assertions.assertEquals(1L, result.getProductId());
    Assertions.assertEquals(124, result.getViewCount());

  }

  @Test
  @DisplayName("상품의 조회수 조회")
  void getProductViewCount() {

    // given
   given(productRepository.findById(anyLong()))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(1L)
            .catId("123456789")
            .productName("test")
            .viewCount(123)
            .build()));

    // when
    var result = viewService.getProductViewCount(1L);

    // then
    Assertions.assertEquals(1L, result.getProductId());
    Assertions.assertEquals("test", result.getProductName());
    Assertions.assertEquals(123, result.getProductViewCount());

  }

  @Test
  @DisplayName("카테고리에 속한 상품들의 총 조회수와 평균 조회수 반환")
  void getCatViewCountAndAvgView() {

    // given
    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(new CategoryEntity("001001000", "카테고리"));
    categories.add(new CategoryEntity("001001001", "하위 카테고리1"));
    categories.add(new CategoryEntity("001001002", "하위 카테고리2"));

    given(categoryRepository.findAllByCatIdLike(anyString()))
        .willReturn(categories);

    List<ProductEntity> products = new ArrayList<>();
    products.add(ProductEntity.builder().productId(1L).viewCount(10).build());
    products.add(ProductEntity.builder().productId(2L).viewCount(20).build());
    products.add(ProductEntity.builder().productId(3L).viewCount(30).build());

    given(productRepository.findAllByCatId(anyString()))
        .willReturn(products);

    // when
    var result = viewService.getCatViewCountAndAvgView("001001000");

    // then
    Assertions.assertEquals("001001000", result.getCatId());
    Assertions.assertEquals(60, result.getCatViewCount());
    Assertions.assertEquals(20, result.getCatViewAvg());

  }

}