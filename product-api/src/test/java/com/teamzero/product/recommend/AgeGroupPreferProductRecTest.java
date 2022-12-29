package com.teamzero.product.recommend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class AgeGroupPreferProductRecTest {

  @MockBean
  private AgeGroupPreferProductRec ageGroupPreferProductRec;

  @Test
  void recommendProducts() {

    // given
    var age = 23;

    // when
    ageGroupPreferProductRec.setUserAge(age);
    var result = ageGroupPreferProductRec.recommendProducts();

    // then
    System.out.println(result);

  }
}