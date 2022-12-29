package com.teamzero.product.client;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
   * 레디스에 데이터 저장
   */
  public <T> boolean addData(LocalDate key, T data) {
    return addData(key.toString(), data);
  }

  public <T> boolean addData(String key, T data) {
    try {
      String value = gson.toJson(data);
      redisTemplate.opsForValue().set(new String(key.getBytes("UTF-8")), value);
      return true;
    } catch(Exception e){
      log.error(e.getMessage());
      return false;
    }
  }

  /**
   * 레디스의 데이터 조회
   */
  public <T> Optional<T> getData(LocalDate key, Class<T> classType) {
    return getData(key.toString(), classType);
  }

  public <T> Optional<T> getData(String key, Class<T> classType) {

    try {

      String value = (String) redisTemplate.opsForValue().get(new String(key.getBytes("UTF-8")));

      if(value == null){
        return Optional.empty();
      }

      return Optional.of(gson.fromJson(value, classType));

    } catch(UnsupportedEncodingException e){

      log.error(e.getMessage());
      return Optional.empty();

    } catch(NullPointerException e) {

      log.error(e.getMessage());
      return Optional.empty();

    }
  }

  //Redis의 데이터 삭제
  public void delete(String key){
      redisTemplate.delete(key);
  }

}
