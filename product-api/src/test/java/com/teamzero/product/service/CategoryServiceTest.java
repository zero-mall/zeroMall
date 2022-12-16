package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.teamzero.product.domain.dto.ProductDetail;
import com.teamzero.product.domain.model.CategoryRegister;
import com.teamzero.product.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  @Test
  void registerCategories() {

    // given
    ProductDetail.Request request = ProductDetail.Request.builder()
        .category1("디지털/가전")
        .category2("PC")
        .category3("조립/베어본PC")
        .naverId("10076181031")
        .title("test")
        .brand("brand")
        .imageUrl("image")
        .lPrice(15000)
        .build();

    given(categoryRepository.existsByCatName(anyString()))
        .willReturn(false);

    // when
    var result = categoryService.categoryRegister(
        CategoryRegister.builder().catName(request.getCategory1()).catType("atype").build()
    );

    // then
    Assertions.assertEquals("디지털/가전", result.getCatName());
    System.out.println(result.getCatId());
  }

}
