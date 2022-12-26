package com.teamzero.product.controller;

import com.mysql.cj.util.StringUtils;
import com.teamzero.product.domain.dto.View;
import com.teamzero.product.service.ProductService;
import com.teamzero.product.service.ViewService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/view")
public class ViewController {

  private final ViewService viewService;

  private final ProductService productService;

  /**
   * 조회수 조회
   * - 요청값 : 카테고리 id, 상품 id
   *   요청값이 하나도 안 들어오거나, 하나만 들어오거나, 모두 들어올 수 있다.
   * - 응답값 :
   *   (1) 요청값이 하나도 안 들어오는 경우, 전체 상품수, 전체 상품의 평균 조회수 반환
   *   (2) 카테고리 id만 들어오는 경우, (1)에, 카테고리의 조회수 정보 추가하여 반환
   *   (3) 상품 id만 들어오는 경우, (1)에, 상품의 조회수 정보 추가하여 반환
   *   (4) 값이 모두 들어오는 경우, (1)에, 카테고리와 상품 조회수 정보 추가하여 반환
   */
  @GetMapping
  public ResponseEntity<?> getAvgProductViewByCategory(View.Request request) {

    var response = new View.Response();

    response.setTotalCnt(productService.countAllProduct());
    response.setAvgTotalView(viewService.getTotalAvgProductView());

    // 요청값이 하나도 안 들어오는 경우
    if (StringUtils.isNullOrEmpty(request.getCatId()) &&
        Objects.isNull(request.getProductId())) {
      return ResponseEntity.ok(response);
    }

    // 카테고리 id값이 들어온 경우
    if (Objects.nonNull(request.getCatId())) {
      response.setCatView(viewService.getCatViewResponse(request.getCatId()));
    }

    // 상품 id값이 들어온 경우
    if (Objects.nonNull(request.getProductId())) {
      response.setProductView(viewService.getProductViewResponse(request.getProductId()));
    }

    return ResponseEntity.ok(response);
  }

}
