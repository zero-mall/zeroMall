package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.mysql.cj.util.StringUtils;
import com.teamzero.product.client.NaverSearchClient;
import com.teamzero.product.domain.dto.product.ProductDetailDto;
import com.teamzero.product.domain.dto.product.ProductSearchDto;
import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.redis.RedisClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private NaverSearchClient naverSearchClient;

  @Mock
  private CategoryService categoryService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private RedisClient redisClient;

  @Mock
  private ViewService viewService;

  @InjectMocks
  private ProductService productService;

  private static final String JSON_SAMPLE =
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
    given(redisClient.getData(anyString(), any()))
        .willReturn(Optional.empty());

    given(naverSearchClient.searchProducts(any()))
        .willReturn(ResponseEntity.ok(JSON_SAMPLE));

    // when
    ProductSearchDto.Response response = productService.searchNaverProducts(new ProductSearchDto.Request("컴퓨터", 1, 10));

    // then
    String title = "게이밍 조립컴퓨터 롤 서든어택 오버워치 배틀그라운드 배그 조립PC 컴퓨터본체";
    Assertions.assertEquals(title, response.getContent().get(0).getTitle());
    Assertions.assertEquals("디지털/가전", response.getContent().get(0).getCategory1());
    Assertions.assertEquals("PC", response.getContent().get(0).getCategory2());
    Assertions.assertEquals("조립/베어본PC", response.getContent().get(0).getCategory3());
    Assertions.assertEquals(29669087L, response.getTotal());
  }

  @Test
  @DisplayName("상품 간략 정보 저장 - DB에 상품이 이미 저장된 경우")
  void getProductShort_alreadySavedInDB() {

    // given
    given(productRepository.findByNaverId(anyString()))
        .willReturn(Optional.of(ProductEntity.builder()
            .productId(1L)
            .catId("123456789")
            .naverId("10076181031")
            .brand("brand")
            .productName("test")
            .imageUrl("image")
            .price(15000)
            .viewCount(123)
            .build()));

    given(viewService.increaseView(anyLong()))
        .willReturn(ProductDetailDto.Response.builder()
            .productId(1L)
            .catId("123456789")
            .naverId("10076181031")
            .brand("brand")
            .name("test")
            .imageUrl("image")
            .price(15000)
            .viewCount(124)
            .build());

    // when
    ProductDetailDto.Response result = productService.createOrJustIncreaseViewProduct(
        ProductDetailDto.Request.builder()
        .category1("디지털/가전")
        .category2("PC")
        .category3("조립/베어본PC")
        .naverId("10076181031")
        .title("test")
        .brand("brand")
        .imageUrl("image")
        .lPrice(15000)
        .build());

    // then
    Assertions.assertEquals(1, result.getProductId());
    Assertions.assertEquals("10076181031", result.getNaverId());
    Assertions.assertEquals("test", result.getName());
    Assertions.assertEquals(124, result.getViewCount());

  }

  @Test
  @DisplayName("상품 간략 정보 저장 - DB에 상품이 없는 경우")
  void getProductShort_noDataInDB() {

    // given
    given(productRepository.findByNaverId(anyString()))
        .willReturn(Optional.empty());

    List<CategoryEntity> categories = new ArrayList<>();
    categories.add(new CategoryEntity("123456788", "카테고리1"));

    given(categoryService.categoryFind(any()))
        .willReturn(categories);

    given(categoryService.categoryRegister(any()))
        .willReturn(new CategoryEntity("123456789", "조립/베어본PC"));

    given(productRepository.save(any()))
        .willReturn(ProductEntity.builder()
            .productId(1L)
            .catId("123456789")
            .naverId("10076181031")
            .brand("brand")
            .productName("test")
            .imageUrl("image")
            .price(15000)
            .viewCount(0)
            .build());

    given(viewService.increaseView(anyLong()))
        .willReturn(ProductDetailDto.Response.builder()
            .productId(1L)
            .catId("123456789")
            .naverId("10076181031")
            .brand("brand")
            .name("test")
            .imageUrl("image")
            .price(15000)
            .viewCount(1)
            .build());

    // when
    var result = productService.createOrJustIncreaseViewProduct(
        ProductDetailDto.Request.builder()
            .category1("디지털/가전")
            .category2("PC")
            .category3("조립/베어본PC")
            .naverId("10076181031")
            .title("test")
            .brand("brand")
            .imageUrl("image")
            .lPrice(15000)
            .build());

    ArgumentCaptor<ProductEntity> captor
        = ArgumentCaptor.forClass(ProductEntity.class);

    // then
    verify(productRepository, times(1)).save(captor.capture());
    Assertions.assertEquals(0, captor.getValue().getViewCount());
    Assertions.assertEquals(1, result.getViewCount());
    Assertions.assertEquals("10076181031", captor.getValue().getNaverId());
    Assertions.assertEquals("test", captor.getValue().getProductName());

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

}