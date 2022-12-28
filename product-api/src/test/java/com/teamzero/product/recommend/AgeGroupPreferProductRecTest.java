package com.teamzero.product.recommend;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.mapper.AgeGroupPreferProductMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgeGroupPreferProductRecTest {

  @Mock
  private AgeGroupPreferProductMapper ageGroupPreferProductMapper;

  @InjectMocks
  private AgeGroupPreferProductRec ageGroupPreferProductRec;

  @Test
  void recommendProducts() {

    // given
    var age = 23;

    given(ageGroupPreferProductMapper.getAgeGroupPreferProducts(anyInt()))
        .willReturn(Arrays.asList(
            ProductEntity.builder()
                .productName("test1").likeCount(320).viewCount(420).build(),
            ProductEntity.builder()
                .productName("test2").likeCount(100).viewCount(650).build()
        ));

    // when
    ageGroupPreferProductRec.setUSER_AGE(age);
    var result = ageGroupPreferProductRec.recommendProducts();

    // then
    result.forEach(e -> System.out.println(
        e.getProductName() + " : 좋아요수 = " + e.getLikeCount() + ", 조회수 = " + e.getViewCount()
    ));
    Assertions.assertEquals("test1", result.get(0).getProductName());
    Assertions.assertEquals(320, result.get(0).getLikeCount());

  }
}