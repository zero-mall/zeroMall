package com.teamzero.member.config;

import com.teamzero.domain.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(){
    return new JwtAuthenticationProvider();
  }

}
