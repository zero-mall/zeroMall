package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.dto.Star;
import com.teamzero.product.domain.model.StarEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.domain.repository.StarRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StarServiceTest {

  @Mock
  private StarRepository starRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private StarService starService;

  @Test
  void createOrModifyStar() {

    // given
    String email = "test1@gmail.com";
    Star.Request request = Star.Request.builder()
        .productId(1L)
        .score(3)
        .build();

    given(memberRepository.existsByEmail(anyString()))
        .willReturn(true);

    given(productRepository.existsByProductId(anyLong()))
        .willReturn(true);

    given(starRepository.findByProductIdAndMemberEmail(any(), anyString()))
        .willReturn(Optional.of(
            StarEntity.builder()
                .starId(1L)
                .score(3)
                .memberEmail(email)
                .build()
        ));

    given(starRepository.countAllByProductId(anyLong()))
        .willReturn(425L);

    given(starRepository.calAvgStarByProductId(anyLong()))
        .willReturn(3.23);

    // when
    Star.Response response = starService.createOrModifyStar(request, email);

    // then
    Assertions.assertEquals(425L, response.getCount());
    Assertions.assertEquals(3.23, response.getAvgStar());

  }

  @Test
  void getAvgStarAndCountByProductId() {

    // given
    given(starRepository.countAllByProductId(anyLong()))
        .willReturn(425L);

    given(starRepository.calAvgStarByProductId(anyLong()))
        .willReturn(3.23);

    // when
    Star.Response response = starService.getAvgStarAndCountByProductId(1L);

    // then
    Assertions.assertEquals(425L, response.getCount());
    Assertions.assertEquals(3.23, response.getAvgStar());

  }
}