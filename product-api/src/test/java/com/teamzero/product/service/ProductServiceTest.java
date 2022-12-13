package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import com.mysql.cj.util.StringUtils;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.dto.NaverProduct;
import com.teamzero.product.domain.dto.ProductSet;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.util.RedisCrud;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private NaverSearchClient naverSearchClient;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private RedisCrud redisCrud;

  @InjectMocks
  private ProductService productService;

  private static String JSON_SAMPLE =
      "{\n"
      + "    \"lastBuildDate\": \"Mon, 12 Dec 2022 21:36:18 +0900\",\n"
      + "    \"total\": 29669087,\n"
      + "    \"start\": 1,\n"
      + "    \"display\": 1,\n"
      + "    \"items\": [\n"
      + "        {\n"
      + "            \"title\": \"게이밍 조립<b>컴퓨터</b> 롤 서든어택 오버워치 배틀그라운드 배그 조립<b>PC</b> <b>컴퓨터</b>본체\",\n"
      + "            \"link\": \"https://search.shopping.naver.com/gate.nhn?id=10076181031\",\n"
      + "            \"image\": \"https://shopping-phinf.pstatic.net/main_1007618/10076181031.40.jpg\",\n"
      + "            \"lprice\": \"430000\",\n"
      + "            \"hprice\": \"\",\n"
      + "            \"mallName\": \"야인컴퓨터\",\n"
      + "            \"productId\": \"10076181031\",\n"
      + "            \"productType\": \"2\",\n"
      + "            \"brand\": \"YAIN\",\n"
      + "            \"maker\": \"야인컴퓨터\",\n"
      + "            \"category1\": \"디지털/가전\",\n"
      + "            \"category2\": \"PC\",\n"
      + "            \"category3\": \"조립/베어본PC\",\n"
      + "            \"category4\": \"\"\n"
      + "        }\n"
      + "    ]\n"
      + "}";

  @Test
  @DisplayName("네이버 상품 검색")
  void searchNaverProducts() {

    // given
    given(naverSearchClient.searchProducts(any()))
        .willReturn(ResponseEntity.ok(JSON_SAMPLE));

    // when
    List<NaverProduct> products = productService.searchNaverProducts("컴퓨터", PageRequest.of(0, 1));

    // then
    String title = "게이밍 조립<b>컴퓨터</b> 롤 서든어택 오버워치 배틀그라운드 배그 조립<b>PC</b> <b>컴퓨터</b>본체";
    Assertions.assertEquals(title, products.get(0).getTitle());
    Assertions.assertEquals("디지털/가전", products.get(0).getCategory1());
    Assertions.assertEquals("PC", products.get(0).getCategory2());
    Assertions.assertEquals("조립/베어본PC", products.get(0).getCategory3());
  }

  @Test
  @DisplayName("캐시(Redis)에서 네이버 상품 조회")
  void getProductFromRedis() {

    // given
    Long naverId = 10076181031L;

    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .catId("111111111")
        .naverId(naverId)
        .name("test")
        .brand("test")
        .imageUrl("url")
        .price(10000)
        .build();

    ProductSet set = new ProductSet();
    set.addProduct(product);

    given(redisCrud.getData(anyString(), any()))
        .willReturn(Optional.of(set));

    // when
    ProductEntity result = productService.getProductFromRedis(naverId);

    // then
    Assertions.assertEquals(product, result);

  }

  @Test
  @DisplayName("DB에서 네이버 상품 조회 또는 저장")
  void getOrCreateProduct() {

    // given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .catId("111111111")
        .naverId(10076181031L)
        .name("test")
        .brand("test")
        .imageUrl("url")
        .price(10000)
        .build();

    given(productRepository.findByProductId(anyLong()))
        .willReturn(Optional.of(product));

    // when
    ProductEntity result = productService.getOrCreateProduct(product);

    // then
    Assertions.assertEquals(product, result);

  }

  @Test
  @DisplayName("상품명 문자열 변환")
  void getNewSearchToString(){

    // given
    String title = "호스트<b>컴</b> 게이밍 조립 <b>컴</b>퓨터 데스크탑 롤 배그 본체 사무용 pc00";

    // when & then
    Set<String> set = new HashSet<>();
    StringBuilder sb = new StringBuilder();

    for (String s : title.split("[\\[\\]]")) {

      if (StringUtils.isNullOrEmpty(s)) continue;

      String newS = s.replaceAll("[(\\<b\\>)(\\</b\\>)]", "");

      if (set.add(newS)) {
        sb.append(newS);
      }
    }

    System.out.println(sb);
  }

  @Test
  @DisplayName("캐시(Redis)에 상품 저장")
  void addToRedis() {

    // given
    ProductEntity product = ProductEntity.builder()
        .productId(1L)
        .catId("111111111")
        .naverId(123452312L)
        .name("test")
        .brand("test")
        .imageUrl("url")
        .price(10000)
        .build();

    given(redisCrud.getData(anyString(), any()))
        .willReturn(Optional.of(new ProductSet()));

    given(redisCrud.saveData(anyString(), any()))
        .willReturn(true);

    // when
    boolean result = productService.addProductToRedisSet(product);

    // then
    Set<ProductEntity> set = redisCrud.getData(CacheKey.NAVER_PRODUCT, ProductSet.class).get().getProducts();
    Assertions.assertEquals(true, result);
    Assertions.assertTrue(set.contains(product));

  }
}