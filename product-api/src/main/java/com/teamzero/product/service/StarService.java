package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.STAR_MEMBER_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.STAR_SCORE_RANGE_NOT_VALID;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.dto.Star;
import com.teamzero.product.domain.dto.Star.Request;
import com.teamzero.product.domain.model.StarEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.domain.repository.StarRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StarService {

  private final StarRepository starRepository;

  private final ProductRepository productRepository;

  private final MemberRepository memberRepository;


  /**
   * 별점 등록 및 수정
   * - 작성자(회원)이 존재하지 않는 경우,
   *   상품이 존재하지 않는 경우, 별점이 1 ~ 5 사이가 아닌 경우 실패 응답
   * - 범위 : 1 ~ 5 (0은 등록되지 않았거나 등록 취소한 상태)
   * - 관련 정책 : 회원이 상품을 구매한 뒤 별점을 등록을 한다.
   *             (이때, 회원이 이미 해당 상품에 별점을 등록한 경우라면 별점 정보를 수정한다.)
   */
  @Transactional
  public Star.Response createOrModifyStar(Star.Request request, String email) {

    // 1. 요청값 및 토큰의 회원 정보가 유효한지 확인
    validateCreateOrModifyStarRequest(request, email);

    // 2. 회원 이메일로 해당 상품에 이미 등록된 별점이 있으면 별점 정보 수정, 없으면 신규 등록
    Optional<StarEntity> optionalStar
        = starRepository.findByProductIdAndMemberEmail(request.getProductId(), email);

    if (optionalStar.isPresent()) {
      optionalStar.get().setScore(request.getScore());
    } else {
      starRepository.save(
          StarEntity.builder()
              .memberEmail(email)
              .productId(request.getProductId())
              .score(request.getScore())
              .build()
      );
    }

    // 3. 상품의 별점 등록수와 누적 평균 정산
    return getAvgStarAndCountByProductId(request.getProductId());
  }

  /**
   * 별점 조회
   * - 상품 ID를 받으면, 해당 상품의 평균 별점과 등록수 반환
   */
  @Transactional(readOnly = true)
  public Star.Response getAvgStarAndCountByProductId(Long productId) {

    long count = starRepository.countAllByProductId(productId);
    double avgStar = starRepository.calAvgStarByProductId(productId);

    return Star.Response.builder()
        .productId(productId)
        .avgStar(avgStar)
        .count(count)
        .build();

  }

  /**
   * 별점 등록 및 수정 validation
   */

  private void validateCreateOrModifyStarRequest(Request request, String email) {
    // 회원 존재 여부 확인
    if (!memberRepository.existsByEmail(email)) {
      throw new TeamZeroException(STAR_MEMBER_NOT_FOUND);
    }

    // 상품 존재 여부 확인
    if (!productRepository.existsByProductId(request.getProductId())) {
      throw new TeamZeroException(PRODUCT_NOT_FOUND);
    }

    // 별점 범위 확인
    if (!(request.getScore() >= 1 && request.getScore() <= 5)) {
      throw new TeamZeroException(STAR_SCORE_RANGE_NOT_VALID);
    }
  }

}
