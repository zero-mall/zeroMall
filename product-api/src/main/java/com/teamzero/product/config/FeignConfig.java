package com.teamzero.product.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignConfig {

  @Value("${naver.search.clientId}")
  String clientId;

  @Value("${naver.search.clientSecret}")
  String clientSecret;

  @Qualifier("naver_search")
  @Bean
  public RequestInterceptor requestInterceptor(){
    return template -> {
      template.header("X-Naver-Client-Id", clientId);
      template.header("X-Naver-Client-Secret", clientSecret);
    };
  }

}
