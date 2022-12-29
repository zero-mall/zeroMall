package com.teamzero.product.recommend;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import com.teamzero.product.mapper.LikeAndStarProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeAndStarProductRec implements ProductRecInterface{

  private final LikeAndStarProductMapper likeAndStarProductMapper;
  /**
   * 한달간 좋아요(최소 100개이상)와 평점의 평균점수가 가장 높은순
   * 
   */
  @Override
  public List<RecommendDto> recommendProducts() {
    return likeAndStarProductMapper.getLikeAndStarProducts();
  }
}
