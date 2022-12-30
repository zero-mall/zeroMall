package com.teamzero.product.redis;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

  private final Gson gson;

  private final RedisTemplate redisTemplate;

  /**
   * 레디스 저장
   */
  public <T> boolean addData(String key, T data) {
    try {
      String value = gson.toJson(data);
      redisTemplate.opsForValue().set(new String(key.getBytes(
          StandardCharsets.UTF_8)), value);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage());
      return false;
    }
  }

  /**
   * 레디스 조회
   */
  public <T> Optional<T> getData(String key, Class<T> classType) {

    try {

      String value = (String) redisTemplate.opsForValue()
          .get(new String(key.getBytes(
              StandardCharsets.UTF_8)));

      if (value == null) {
        return Optional.empty();
      }

      return Optional.of(gson.fromJson(value, classType));

    } catch (NullPointerException e) {

      log.error(e.getMessage());
      return Optional.empty();

    }
  }

  /**
   * 레디스 삭제
   */
  public void delete(String key) {
    redisTemplate.delete(key);
  }

}
