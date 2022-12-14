package com.teamzero.product.service;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.ProductOfMallEntity;
import com.teamzero.product.scraper.ElevenShopScraper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductScraperTest {

  @InjectMocks
  private ElevenShopScraper elevenShopScraper;
  @Mock
  private ProductEntity productEntity;

  @Test
  @DisplayName("11번가 상품정보 성공 테스트")
  void ElevenShopScraperTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1)
        .productName("아이닉 new i20")
        .standPrice(263340)
        .build();

    //when
    List<ProductOfMallEntity> product1 = elevenShopScraper.getScrapProductList(product);
    //then
    for(ProductOfMallEntity item : product1){
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("11번가 없는 상품 테스트")
  void ElevenShopScraperEmptyTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1)
        .productName("가나다라마바사아자")
        .standPrice(1)
        .build();

    //when
    List<ProductOfMallEntity> product1 = elevenShopScraper.getScrapProductList(product);
    //then
    assertNull(product1);
  }

}
