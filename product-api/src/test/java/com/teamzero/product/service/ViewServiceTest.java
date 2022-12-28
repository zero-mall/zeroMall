package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import com.teamzero.product.domain.dto.View;
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
  @DisplayName("특정 상품의 조회수 조회")
  void getProductViewResponse() {

    // given
    var request = new View.Request("", 1L);

    given(productRepository.findById(anyLong()))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(1L)
            .catId("123456789")
            .naverId("10076181031")
            .brand("brand")
            .imageUrl("image")
            .price(15000)
            .viewCount(123)
            .build()));

    // when
    var result = viewService.getProductViewResponse(request.getProductId());

    // then
    Assertions.assertEquals(1L, result.getProductId());
    Assertions.assertEquals(123, result.getProductView());

  }

  @Test
  @DisplayName("특정 카테고리의 평균 조회수 조회")
  void getCatViewResponse() {

    // given
    var request = new View.Request("001001000", null);

    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(new CategoryEntity("001001000", "카테고리1"));
    categories.add(new CategoryEntity("001001001", "카테고리2"));
    categories.add(new CategoryEntity("001001002", "카테고리3"));

    given(categoryRepository.findAllByCatIdLike(anyString()))
        .willReturn(categories);

    given(productRepository.findByCatId("001001000"))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(1L)
            .catId("001001000")
            .naverId("10076181031")
            .brand("brand")
            .imageUrl("image")
            .price(12000)
            .viewCount(123)
            .build()));

    given(productRepository.findByCatId("001001001"))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(2L)
            .catId("001001001")
            .naverId("10076181032")
            .brand("brand")
            .imageUrl("image")
            .price(25400)
            .viewCount(123)
            .build()));

    given(productRepository.findByCatId("001001002"))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(3L)
            .catId("001001002")
            .naverId("10076181033")
            .brand("brand")
            .imageUrl("image")
            .price(1000)
            .viewCount(123)
            .build()));

    // when
    var result = viewService.getCatViewResponse(request.getCatId());

    // then
    System.out.printf("catId : %s, size : %d, avg : %f\n",
        result.getCatId(), result.getTotalCatCnt(), result.getAvgCatView());
    Assertions.assertEquals("001001000", result.getCatId());

  }

}