//package com.teamzero.product.controller;
//
//import com.teamzero.product.domain.dto.Star;
//import com.teamzero.product.domain.dto.Star.Response.StarInfo;
//import com.teamzero.product.domain.model.constants.CacheKey;
//import lombok.RequiredArgsConstructor;
//import lombok.var;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/member/star")
//public class StarController {
//
//  private final StarService starService;
//
//  /**
//   * 별점 등록
//   * - 범위 : 0 ~ 5
//   * - 정책 : 레디스 캐시를 사용해 매일 오전 2시에 일괄 등록한다.
//   */
//  @PostMapping("/create")
//  @Cacheable(key = "#request.productId", value = CacheKey.STAR)
//  public ResponseEntity<Star.Response> createStar(@RequestBody Star.Request request) {
//
//    // 1. 레디스에 별점을 등록하고, 상품에 연결된 별점 집합을 반환
//    var result = starService.createStar(request);
//
//    // 2.
//    var response = new Star.Response();
//
//    return ResponseEntity.ok(response);
//
//  }
//
//  /**
//   * 별점 조회
//   * - 상품의 평균 별점과 등록자수 반환
//   * - 정책 : 금일 오전 2시 기점 상품 평균 별점 반환
//   */
//  @GetMapping
//  public ResponseEntity<Star.Response> getStarByProductId(@RequestParam Long productId) {
//
//    var response = starService.getStarByProductId(starId);
//
//    return ResponseEntity.ok(response);
//
//  }
//
//  /**
//   * 별점 수정
//   * - 매개변수 : 회원 id, 상품 id
//   * - 정책 : 레디스 캐시에 저장된
//   */
//  @PutMapping
//  public ResponseEntity<Star.Response> modifyStarById(Star.Request request) {
//
//    var response = starService.modifyStarByStarId(starId);
//
//    return ResponseEntity.ok().build();
//
//  }
//
//  /**
//   * 별점 삭제
//   */
//
//
//}
