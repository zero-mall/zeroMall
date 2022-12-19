package com.teamzero.product.client;

import com.teamzero.product.client.RedisClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisClientTest {

  @Autowired
  private RedisClient redisClient;

  @Test
  void test() {

    // given
    String key = "hello";
    String value = "문자열";

    // when
    redisClient.addData(key, value);

    // then
    System.out.println(redisClient.getData(key, String.class));

  }

}