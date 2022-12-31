package com.teamzero.product.controller;

import com.teamzero.product.domain.dto.mall.PriceCompareDto;
import com.teamzero.product.service.MallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mall")
public class MallController {

  private final MallService mallService;


  /**
   * 쇼핑몰 최저가 비교
   */
  @GetMapping("/price-compare/{productId}")
  public ResponseEntity<PriceCompareDto> compareMallProducts(
      @PathVariable Long productId, @RequestParam int pageSize,
      @RequestParam int pageNumber) {

    return ResponseEntity.ok(mallService.compareProductsFromMalls(
        productId, pageSize, pageNumber));
  }

}
