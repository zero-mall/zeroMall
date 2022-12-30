package com.teamzero.product.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class RedisConfigTest {

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  @DisplayName("레디스 템플릿 테스트 : String 저장")
  void test1() {

    // given
    ValueOperations<String, Object> string = redisTemplate.opsForValue();

    // when
    string.set("test1", "testValue");

    String test1 = (String)string.get("test1");

    // then
    System.out.println(test1);
    Assertions.assertEquals(test1, "testValue");

  }

  @Test
  @DisplayName("레디스 템플릿 테스트 : List 저장")
  void test2() {

    // given
    ListOperations<String, String> list = redisTemplate.opsForList();

    // when
    list.rightPush("list1", "test1");
    list.rightPush("list1", "test2");

    String list1 = list.leftPop("list1");

    // then
    System.out.println(list1);

  }

}