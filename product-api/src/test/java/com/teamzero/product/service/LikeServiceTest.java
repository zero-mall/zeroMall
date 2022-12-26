package com.teamzero.product.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.product.domain.model.LikeEntity;
import com.teamzero.product.domain.repository.LikeRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("상품 좋아요 테스트")
    void addLike() {

        // given
        String email = "test1@gmail.com";
        Long productId = 1L;

        given(memberRepository.existsByEmail(anyString()))
            .willReturn(true);

        given(productRepository.existsByProductId(anyLong()))
            .willReturn(true);

        // when
        boolean response = likeService.addLike(email, productId);

        // then
        Assertions.assertEquals(true, response);

    }

    @Test
    @DisplayName("상품 좋아요 중복 테스트")
    void addTwiceLike() {

        // given
        String email = "test1@gmail.com";
        Long productId = 1L;

        given(memberRepository.existsByEmail(anyString()))
            .willReturn(true);

        given(productRepository.existsByProductId(anyLong()))
            .willReturn(true);

        given(likeRepository.findByMemberEmailAndProductId(anyString(),
            anyLong()))
            .willReturn(Optional.of(
                LikeEntity.builder()
                    .likeId(1L)
                    .memberEmail(email)
                    .build()
            ));

        // then
        Assertions.assertThrows(TeamZeroException.class, () -> {
            likeService.addLike(email, productId);
        });


    }

    @Test
    @DisplayName("상품 좋아요 갯수 테스트")
    void countLikes() {

        // given
        given(likeRepository.countAllByProductId(anyLong()))
            .willReturn(123L);

        // when
        Long response = likeService.likeCount(1L);

        // then
        Assertions.assertEquals(123L, response);

    }

    @Test
    @DisplayName("상품 좋아요 취소 테스트")
    void cancelLike() {

        // given
        String email = "test1@gmail.com";
        Long productId = 1L;

        given(memberRepository.existsByEmail(anyString()))
            .willReturn(true);

        given(productRepository.existsByProductId(anyLong()))
            .willReturn(true);
        given(likeRepository.findByMemberEmailAndProductId(anyString(),
            anyLong()))
            .willReturn(Optional.of(
                LikeEntity.builder()
                    .likeId(1L)
                    .memberEmail(email)
                    .build()
            ));
        // when
        boolean response = likeService.cancelLike(email, productId);

        // then
        Assertions.assertEquals(true, response);

    }
}