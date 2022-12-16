package com.teamzero.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mall")
public class MallController {

  /**
   * 쇼핑몰 최저가 비교
   */
  @GetMapping("/api/compare/{productId}")
  public ResponseEntity<?> compareMallProducts(@PathVariable Long productId){



    return null;
  }

}
