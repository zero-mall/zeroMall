package com.teamzero.product.scraper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DanawaShopProductScraperTest {

  @InjectMocks
  private DanawaProductScraper danawaProductScraper;

  @Test
  @DisplayName("다나와 상품정보 성공 테스트")
  void ShopScraperTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .name("삼성전자 제트 VS20T92P7SD")
        .price(1399000)
        .build();

    //when
    List<MallProductEntity> product1 = danawaProductScraper.getScrapProductList(product);
    //then
    for(MallProductEntity item : product1){
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("다나와 없는 상품 테스트")
  void ShopScraperEmptyTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .name("가나다라마바사아자")
        .price(1)
        .build();
    //when
    List<MallProductEntity> product1 = danawaProductScraper.getScrapProductList(product);
    //then
    assertEquals(product1.size(),0);
  }

}
