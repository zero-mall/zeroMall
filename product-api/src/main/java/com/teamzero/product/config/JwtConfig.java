package com.teamzero.product.config;

import com.teamzero.domain.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 로그인 시 JWT 를 통한 인증
 */
@Configuration
public class JwtConfig {

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(){
    return new JwtAuthenticationProvider();
  }

}
