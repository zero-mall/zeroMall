package com.teamzero.product.recommend;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallProductRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowestPriceProductRec implements ProductRecInterface {

  private final ProductRepository productRepository;
  private final MallProductRepository mallProductRepository;

  /**
   * 한달 이내 최대 낙폭의 상품 상위 5개 반환
   */
  @Override
  public List<RecommendDto> recommendProducts() {

    HashMap<Long, Double> productMap = new HashMap<>();
    List<RecommendDto> recommendList = new ArrayList<>();

    // 한달 이내에 등록, 수정된 상품만 불러오기
    LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
    List<ProductEntity> productEntities =
        productRepository.findAllByRegisteredAtAfterOrModifiedAtAfter(
            monthAgo, monthAgo
        );

    // 상품의 갯수만큼 돌면서 퍼센트 계산
    for (int i = 0; i < productEntities.size(); i++) {

      long productId = productEntities.get(i).getProductId();
      RecommendDto recommendDtoTmp = currentPricePercentage(productId);
      recommendList.add(recommendDtoTmp);
      // productId 는 MallProductEntityId로 잠깐 사용
      productMap.put(productId, recommendDtoTmp.getPerc());

    }

    List<Long> keyList = new ArrayList<>(productMap.keySet());

    // Collections 의 sort 활용하여 퍼센테이지별로 정렬
    Collections.sort(keyList,
        (v1, v2) -> (productMap.get(v1).compareTo(productMap.get(v2))));

    // 가장 퍼센테이지가 높은 상품 5개만 RecommendList 로반환
    for (int i = 0; i < 5; i++) {
      Optional<MallProductEntity> product =
          mallProductRepository.findById(keyList.get(i));

      recommendList.add(RecommendDto.builder()
          .productId(product.get().getProductId())
          .productName(product.get().getName())
          .imageUrl(product.get().getImageUrl())
          .price(product.get().getPrice())
          .maxPrice(product.get().getMaxPrice())
          .perc(productMap.get(keyList.get(i)))
          .build());
    }

    return recommendList;

  }

  public RecommendDto currentPricePercentage(Long productId) {

    List<MallProductEntity> mallProductEntities =
        mallProductRepository.findAllByProductId(productId);

    HashMap<Long, Double> productMap = new HashMap<>();

    for (int i = 0; i < mallProductEntities.size(); i++) {

      long mallProductId = mallProductEntities.get(i).getId();
      int price = mallProductEntities.get(i).getPrice();
      int maxPrice = mallProductEntities.get(i).getMaxPrice();

      //퍼센테이지 구하는 공식
      double perc =
          (double) price / (double) maxPrice * 100.0 - 100.0;

      // 0퍼센트 혹은 -100%인 경우를 제외하고 put
      if (perc < 0 && perc > -100) {
        productMap.put(mallProductId, perc);
      }
    }

    List<Long> keyList = new ArrayList<>(productMap.keySet());

    // Collections 의 sort 활용하여 퍼센테이지별로 정렬
    Collections.sort(keyList,
        (v1, v2) -> (productMap.get(v1).compareTo(productMap.get(v2))));

    // MallProductEntity Id 활용하여 상품특정
    Optional<MallProductEntity> product = mallProductRepository.findById(
        keyList.get(0));

    // RecommendDto활용하여 MallProductEntity Id와 퍼센테이지 반환
    return RecommendDto.builder()
        .productId(product.get().getId())
        .perc(productMap.get(
            keyList.get(0)))
        .build();

  }

}
