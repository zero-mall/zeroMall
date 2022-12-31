package com.teamzero.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 구독자에게 메일 전송하기 위해 JavaMailConfig 사용
 */
@Configuration
public class JavaMailConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    return new JavaMailSenderImpl();
  }

}
