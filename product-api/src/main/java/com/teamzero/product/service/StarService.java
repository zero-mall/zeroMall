package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.STAR_MEMBER_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.STAR_SCORE_RANGE_NOT_VALID;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.dto.StarDto;
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
   * - 이미 등록된 별점인 경우 기존 별점을 수정
   * - 범위 : 1 ~ 5 (0은 등록되지 않았거나 등록 취소한 상태)
   * - 실패 응답 : 작성자(회원)이 존재하지 않는 경우,
   *             상품이 존재하지 않는 경우, 별점이 1 ~ 5 사이가 아닌 경우 실패 응답
   */
  @Transactional
  public StarDto createOrModifyStar(Long productId, String email, int score) {

    // 1. 요청값이 유효한 값인지 확인
    validateCreateOrModifyStarRequest(productId, email, score);

    // 2. 해당 상품에 회원이 별점을 등록했었는지 조회
    Optional<StarEntity> optionalStar
        = starRepository.findByProductIdAndEmail(productId, email);

    // 3. 이미 등록을 했다면 수정하고, 그렇지 않으면 신규 저장
    if (optionalStar.isPresent()) {
      optionalStar.get().setScore(score);
    } else {
      starRepository.save(
          StarEntity.builder()
              .productId(productId)
              .email(email)
              .score(score)
              .build()
      );
    }

    // 4. 상품 별점 평균과 별점 등록 갯수 반환
    return getAvgStarAndCountByProductId(productId);
  }

  /**
   * 별점 등록 및 수정 validation
   */

  private void validateCreateOrModifyStarRequest(
      Long productId, String email, int score) {

    // 상품이 존재하지 않는 경우
    if (!productRepository.existsByProductId(productId)) {
      throw new TeamZeroException(PRODUCT_NOT_FOUND);
    }

    // 회원이 존재하지 않는 경우
    if (!memberRepository.existsByEmail(email)) {
      throw new TeamZeroException(STAR_MEMBER_NOT_FOUND);
    }

    // 별점의 범위가 1 ~ 5 사이가 아닌 경우
    if (!(score >= 1 && score <= 5)) {
      throw new TeamZeroException(STAR_SCORE_RANGE_NOT_VALID);
    }
  }

  /**
   * 별점 조회
   * - 상품 ID를 받으면, 해당 상품의 평균 별점과 등록수 반환
   */
  @Transactional(readOnly = true)
  public StarDto getAvgStarAndCountByProductId(Long productId) {

    // 총 별점 갯수
    long count = starRepository.countAllByProductId(productId);
    
    // 별점 평균
    double avgStar = starRepository.calAvgStarByProductId(productId);

    return StarDto.builder()
        .productId(productId)
        .starAvg(avgStar)
        .starCount(count)
        .build();

  }

}
