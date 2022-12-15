package com.teamzero.product.util;

import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.dto.RedisProductSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisCrudTest {

  @Autowired
  private RedisCrud redisCrud;

  @Test
  void test() {

    // given
    String key = "hello";
    String value = "문자열";

    // when
    redisCrud.saveData(key, value);

    // then
    System.out.println(redisCrud.getData(key, String.class));

  }

  @Test
  void test2(){

    // given
    RedisProductSet set = new RedisProductSet();
    ProductEntity product1 = ProductEntity.builder()
        .productId(1L)
        .catId("111111111")
        .naverId(123452312L)
        .name("test")
        .brand("test")
        .imageUrl("url")
        .price(10000)
        .build();
    ProductEntity product2 = ProductEntity.builder()
        .productId(2L)
        .catId("222222222")
        .naverId(123452312L)
        .name("test")
        .brand("test")
        .imageUrl("url")
        .price(10000)
        .build();
    set.addProduct(product1);
    set.addProduct(product2);

    // when
    redisCrud.saveData(CacheKey.NAVER_PRODUCT, set);

    // then
    Optional<RedisProductSet> test = redisCrud.getData(CacheKey.NAVER_PRODUCT, RedisProductSet.class);
    System.out.println(test.isPresent());
    test.get().getProducts().forEach(System.out::println);

  }


}