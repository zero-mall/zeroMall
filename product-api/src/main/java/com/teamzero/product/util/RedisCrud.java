package com.teamzero.product.util;

import com.google.gson.Gson;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCrud {

  private final Gson gson;

  private final RedisTemplate redisTemplate;

  /**
   * 레디스에 데이터 저장
   */
  public <T> boolean saveData(String key, T data) {
    try {
      String value = gson.toJson(data);
      redisTemplate.opsForValue().set(key, value);
      return true;
    } catch(Exception e){
      log.error(e.getMessage());
      return false;
    }
  }

  /**
   * 레디스의 데이터 조회
   */
  public <T> Optional<T> getData(String key, Class<T> classType) {

    String value = (String) redisTemplate.opsForValue().get(key);

    if(value == null){
      return Optional.empty();
    }

    try {
      return Optional.of(gson.fromJson(value, classType));
    } catch(Exception e){
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

}
