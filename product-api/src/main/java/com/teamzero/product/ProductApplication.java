package com.teamzero.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"com.teamzero.product.domain.repository", "com.teamzero.member.domain.repository"})
@EntityScan(basePackages = {"com.teamzero.product.domain.model", "com.teamzero.member.domain.model"})
@EnableScheduling
@EnableJpaAuditing
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}