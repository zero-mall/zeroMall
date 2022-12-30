package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.dto.StarDto;
import com.teamzero.product.domain.model.StarEntity;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.domain.repository.StarRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  @DisplayName("별점 등록 및 수정 : DB에 별점이 이미 저장된 경우")
  void createOrModifyStar_alreadyInDB() {

    // given
    given(productRepository.existsByProductId(anyLong()))
        .willReturn(true);

    given(memberRepository.existsByEmail(anyString()))
        .willReturn(true);

    given(starRepository.findByProductIdAndEmail(any(), anyString()))
        .willReturn(Optional.of(
            StarEntity.builder()
                .starId(1L)
                .score(3)
                .email("test1@gmail.com")
                .build()
        ));

    given(starRepository.countAllByProductId(anyLong()))
        .willReturn(425L);

    given(starRepository.calAvgStarByProductId(anyLong()))
        .willReturn(3.23);

    // when
    StarDto response
        = starService.createOrModifyStar(1L, "test1@gmail.com", 3);

    // then
    Assertions.assertEquals(1L, response.getProductId());
    Assertions.assertEquals(425L, response.getStarCount());
    Assertions.assertEquals(3.23, response.getStarAvg());

  }

  @Test
  @DisplayName("별점 등록 및 수정 : DB에 등록된 별점이 없는 경우")
  void createOrModifyStar_noDataInDB() {

    // given
    given(productRepository.existsByProductId(anyLong()))
        .willReturn(true);

    given(memberRepository.existsByEmail(anyString()))
        .willReturn(true);

    given(starRepository.findByProductIdAndEmail(any(), anyString()))
        .willReturn(Optional.of(
            StarEntity.builder()
                .starId(1L)
                .score(3)
                .email("test1@gmail.com")
                .build()
        ));

//    given(starRepository.save(any()))
//        .willReturn(Optional.of(
//            StarEntity.builder()
//                .starId(1L)
//                .score(4)
//                .email("test1@gmail.com")
//                .build()
//        ));

    given(starRepository.countAllByProductId(anyLong()))
        .willReturn(425L);

    given(starRepository.calAvgStarByProductId(anyLong()))
        .willReturn(3.23);

    // when
    StarDto response
        = starService.createOrModifyStar(1L, "test1@gmail.com", 3);

   // then
    Assertions.assertEquals(425L, response.getStarCount());
    Assertions.assertEquals(3.23, response.getStarAvg());

  }

  @Test
  @DisplayName("상품의 평균 별점과 별점 등록 갯수 반환")
  void getAvgStarAndCountByProductId() {

    // given
    given(starRepository.countAllByProductId(anyLong()))
        .willReturn(425L);

    given(starRepository.calAvgStarByProductId(anyLong()))
        .willReturn(3.23);

    // when
    StarDto response = starService.getAvgStarAndCountByProductId(1L);

    // then
    Assertions.assertEquals(425L, response.getStarCount());
    Assertions.assertEquals(3.23, response.getStarAvg());

  }
}