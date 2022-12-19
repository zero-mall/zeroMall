package com.teamzero.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallRepository;
import com.teamzero.product.scraper.AkmallScraper;
import com.teamzero.product.scraper.ElevenShopScraper;
import com.teamzero.product.scraper.GmarketScraper;
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
  @InjectMocks
  private GmarketScraper gmarketScraper;
  @InjectMocks
  private AkmallScraper aKmallScraper;
  @Mock
  private ProductEntity productEntity;
  @Mock
  private MallRepository mallRepository;

  @Test
  @DisplayName("11번가 상품정보 성공 테스트")
  void ElevenShopScraperTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .productName("아이닉 new i20")
        .price(263340)
        .build();

    MallEntity mall = MallEntity.builder().mallId(1L)
            .name("11번가")
        .build();

    mallRepository.save(mall);

    var test = mallRepository.findAllBymallId(1L);

    //when
    List<MallProductEntity> product1 = elevenShopScraper.getScrapProductList(product);
    //then
    for(MallProductEntity item : product1){
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("11번가 없는 상품 테스트")
  void ElevenShopScraperEmptyTest(){
    //given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .productName("가나다라마바사아자")
        .price(1)
        .build();
    //when
    List<MallProductEntity> product1 = elevenShopScraper.getScrapProductList(product);
    //then
    assertEquals(product1.size(),0);
  }

    @Test
    @DisplayName("Gmarket 상품정보 성공 테스트")
    void GmarketScraperTest() {
        //given
        ProductEntity product = ProductEntity.builder()
            .productId(1L)
            .productName("LG그램 17")
            .price(1950000)
            .build();

        MallEntity mall = MallEntity.builder().mallId(2L)
            .name("G마켓")
            .build();

        mallRepository.save(mall);

        //when
        List<MallProductEntity> product1 = gmarketScraper.getScrapProductList(
            product);
        //then
        for (MallProductEntity item : product1) {
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName("Gmarket 없는 상품 테스트")
    void GmarketScraperEmptyTest(){
        //given
        ProductEntity product = ProductEntity.builder()
            .productId(1L)
            .productName("LG그램 112345647")
            .price(1950000000)
            .build();
        //when
        List<MallProductEntity> product1 = gmarketScraper.getScrapProductList(product);
        //then
        assertEquals(product1,null);
    }
    @Test
    @DisplayName("AKmall 상품정보 성공 테스트")
    void AkmallScraperTest() {
        //given
        ProductEntity product = ProductEntity.builder()
            .productId(1L)
            .productName("LG그램 17")
            .price(1950000)
            .build();

        MallEntity mall = MallEntity.builder().mallId(4L)
            .name("AK몰")
            .build();

        mallRepository.save(mall);

        //when
        List<MallProductEntity> product1 = aKmallScraper.getScrapProductList(
            product);
        //then
        for (MallProductEntity item : product1) {
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName("AKmall 없는 상품 테스트")
    void AkmallScraperEmptyTest(){
        //given
        ProductEntity product = ProductEntity.builder()
            .productId(1L)
            .productName("LG그램 112345647")
            .price(1950000000)
            .build();
        //when
        List<MallProductEntity> product1 = aKmallScraper.getScrapProductList(product);
        //then
        assertEquals(product1,null);
    }
}
