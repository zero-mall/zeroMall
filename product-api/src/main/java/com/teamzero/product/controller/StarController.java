package com.teamzero.product.controller;

import static com.teamzero.member.exception.ErrorCode.TOKEN_NOT_VALID;
import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.domain.UserVo;
import com.teamzero.member.exception.TeamZeroException;
import com.teamzero.product.domain.dto.Star;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.service.StarService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/star")
public class StarController {

  private final StarService starService;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  /**
   * 별점 등록
   * - 범위 : 1 ~ 5 (0은 등록되지 않았거나 등록 취소한 상태)
   * - 관련 정책 : 회원이 상품을 구매한 뒤 별점을 등록을 한다.
   *             (이때, 회원이 이미 해당 상품에 별점을 등록한 경우라면 별점 정보를 수정한다.)
   */
  @PostMapping("/create")
  @Cacheable(key = "#request.productId", value = CacheKey.STAR)
  public ResponseEntity<Star.Response> createStar(@RequestHeader(name = "Authentication") String token,
                                                  @RequestBody Star.Request request) {

    // 토큰 확인
    if (!jwtAuthenticationProvider.validToken(token)) {
      throw new TeamZeroException(TOKEN_NOT_VALID);
    }

    // 토큰에서 회원 정보 얻기
    UserVo vo = jwtAuthenticationProvider.getUserVo(token);

    // 별점 DB에 별점을 등록 & 상품 DB와 레디스에서 연결된 상품의 평균 별점 정산
    Star.Response response = starService.createOrModifyStar(request, vo.getEmail());

    return ResponseEntity.ok(response);

  }

  /**
   * 별점 조회
   * - 상품 ID를 받으면, 해당 상품의 평균 별점과 등록수 반환
   */
  @GetMapping
  public ResponseEntity<Star.Response> getStarByProductId(@RequestParam Long productId) {

    // 상품의 평균 별점과 등록자수 반환
    Star.Response response = starService.getAvgStarAndCountByProductId(productId);

    return ResponseEntity.ok().build();

  }

  /**
   * 별점 수정
   * - 매개변수 : 회원 이메일, 상품 id
   * - 관련 정책 :
   *   별점 등록 취소 시 0점을,
   *   1 ~ 5점 사이로 수정 요청시 해당 점수를 반영한다.
   */
  @PutMapping
  public ResponseEntity<Star.Response> modifyStarById(@RequestHeader(name = "Authentication") String token,
                                                      @RequestBody Star.Request request) {

    // 토큰 확인
    if (!jwtAuthenticationProvider.validToken(token)) {
      throw new TeamZeroException(TOKEN_NOT_VALID);
    }

    // 토큰에서 회원 정보 얻기
    UserVo vo = jwtAuthenticationProvider.getUserVo(token);

    // 별점 수정
    Star.Response response = starService.createOrModifyStar(request, vo.getEmail());

    return ResponseEntity.ok(response);

  }



}
