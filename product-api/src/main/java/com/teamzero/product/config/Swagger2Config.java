package com.teamzero.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 설정
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket swagger(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("상품 API")
            .description(
                "네이버 상품을 기준으로 각 쇼핑몰에서 동일 상품을 검색하여 최저가를 비교하고, "
                    + "구독자를 대상으로 상품을 추천하는 서비스를 제공합니다.")
            .version("1.0")
            .build();
    }

}
