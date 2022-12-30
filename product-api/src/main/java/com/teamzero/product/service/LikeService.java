package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.LIKE_ALREADY_LIKED;
import static com.teamzero.product.exception.ErrorCode.LIKE_ALREADY_UNLIKED;
import static com.teamzero.product.exception.ErrorCode.LIKE_MEMBER_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.model.LikeEntity;
import com.teamzero.product.domain.repository.LikeRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;

  private final ProductRepository productRepository;

  private final MemberRepository memberRepository;


  /**
   * 좋아요 등록
   */
  public boolean addLike(String email, Long productId) {
    // 회원 정보와 상품정보 유효한지 확인(고은님코드 참고하였습니다)
    validateRequest(email, productId);

    // 해당 회원이 좋아요를 누른 상품인지 확인 후, 이미 좋아요 누른 상품이면 오류반환
    Optional<LikeEntity> optionalLike
        = likeRepository.findByMemberEmailAndProductId(email, productId);

    if (!optionalLike.isPresent()) {
      likeRepository.save(
          LikeEntity.builder()
              .productId(productId)
              .memberEmail(email)
              .build()
      );
      return true;
    } else {
      throw new TeamZeroException(LIKE_ALREADY_LIKED);
    }
  }

  /**
   * 좋아요 조회 - 상품 ID를 받아서 좋아요 갯수 반환
   */
  public Long countLikes(Long productId) {

    return likeRepository.countAllByProductId(productId);

  }

  /**
   * 좋아요 취소
   */
  public boolean cancelLike(String email, Long productId) {
    // 회원 정보와 상품정보 유효한지 확인(고은님코드 참고하였습니다)
    validateRequest(email, productId);

    // 해당 회원이 좋아요를 누른 상품인지 확인 후 이미 좋아요 취소한 상품이면 오류반환
    Optional<LikeEntity> optionalLike
        = likeRepository.findByMemberEmailAndProductId(email, productId);

    if (optionalLike.isPresent()) {
      likeRepository.deleteById(optionalLike.get().getLikeId());
      return true;
    } else {
      throw new TeamZeroException(LIKE_ALREADY_UNLIKED);
    }

  }

  /**
   * 회원 정보와 상품정보 유효한지 확인
   */
  private void validateRequest(String email, Long productId) {
    // 회원 존재 여부 확인
    if (!memberRepository.existsByEmail(email)) {
      throw new TeamZeroException(LIKE_MEMBER_NOT_FOUND);
    }

    // 상품 존재 여부 확인
    if (!productRepository.existsByProductId(productId)) {
      throw new TeamZeroException(PRODUCT_NOT_FOUND);
    }

  }

}
