package com.teamzero.product.recommend;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import com.teamzero.product.mapper.AgeGroupPreferProductMapper;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class AgeGroupPreferProductRec implements ProductRecInterface {

  private final AgeGroupPreferProductMapper ageGroupPreferProductMapper;

  private int userAgeGroup;

  /**
   * 같은 연령대의 고객들이 선호하는 상품 추천
   * - 지난 한달 동안 같은 연령대의 고객들에게 평균 3.0 이상의 별점을 받은 상품들 중,
   *   좋아요수가 가장 높은 5개의 상품을 반환 (좋아요수가 같을 시 조회수가 높은 상품이 먼저)
   */
  @Override
  public List<RecommendDto> recommendProducts() {

    return ageGroupPreferProductMapper.getAgeGroupPreferProducts(userAgeGroup);

  }

  public void setUserAge(int age) {
    this.userAgeGroup = (int)(Math.floor(age / 10) * 10);
  }

}
