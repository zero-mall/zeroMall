package com.teamzero.product.controller;

import com.mysql.cj.util.StringUtils;
import com.teamzero.product.domain.dto.ViewDto;
import com.teamzero.product.exception.ErrorCode;
import com.teamzero.product.exception.TeamZeroException;
import com.teamzero.product.service.ProductService;
import com.teamzero.product.service.ViewService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/view")
public class ViewController {

  private final ViewService viewService;

  private final ProductService productService;

  /**
   * 전체 조회수 조회
   * - 특정 카테고리의 전체 조회수 & 평균 조회수를 조회하거나,
   *   특정 상품의 전체 조회수를 조회하는 방식
   */
  @GetMapping
  public ResponseEntity<ViewDto> getAvgProductViewByCategory(
      @RequestParam String catId, @RequestParam Long productId) {

    // 요청값이 하나도 안 들어오는 경우
    if (StringUtils.isNullOrEmpty(catId) && Objects.isNull(productId)) {
      throw new TeamZeroException(ErrorCode.VIEW_SEARCH_BAD_REQUEST);
    }

    // 카테고리 id값만 들어온 경우
    if (!StringUtils.isNullOrEmpty(catId) && Objects.isNull(productId)) {

      return ResponseEntity.ok(viewService.getCatViewCountAndAvgView(catId));
    }

    // 상품 id값만 들어온 경우
    if (StringUtils.isNullOrEmpty(catId) && !Objects.isNull(productId)) {

      return ResponseEntity.ok(viewService.getProductViewCount(productId));
    }

    // 카테고리 id값과 상품 id값이 들어온 경우
    return ResponseEntity.ok(viewService.getProductViewCount(productId));
  }

}
