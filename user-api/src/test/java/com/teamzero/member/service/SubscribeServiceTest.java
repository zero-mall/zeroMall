package com.teamzero.member.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private SubscribeService subscribeService;

    @Test
    @DisplayName("구독하기")
    void addSubscribe() {
        //given
        String email = "test@gmail.com";
        String grade = "PREMIUM";
        MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .email("test@gmail.com")
            .memberGradeEntity(MemberGradeEntity.builder()
                .id(1L)
                .name("BASIC")
                .rewardPointPct(0).build())
            .memberStatus(MemberStatus.IN_USE)
            .build();

        given(memberRepository.findByEmail(email))
            .willReturn(Optional.of(member));

        // when
        boolean result = subscribeService.add(member.getEmail(),
            grade);

        // then
        Assertions.assertEquals(true, result);

    }

    @Test
    @DisplayName("구독 수정")
    void modifySubscribe() {

        // given
        MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .email("test@gmail.com")
            .memberGradeEntity(MemberGradeEntity.builder()
                .id(1L)
                .name("BASIC")
                .rewardPointPct(0).build())
            .memberStatus(MemberStatus.IN_USE)
            .build();
        MemberGradeEntity memberGrade = MemberGradeEntity.builder()
            .id(1L)
            .member(member)
            .name("SILVER")
            .rewardPointPct(1)
            .build();
        String grade = "PREMIUM";

        given(memberRepository.findByEmail(member.getEmail()))
            .willReturn(Optional.of(member));
        given(memberGradeRepository.findById(anyLong()))
            .willReturn(Optional.of(memberGrade));

        // when
        boolean result = subscribeService.modify(member.getEmail(), 1L,
            grade);

        // then
        Assertions.assertEquals(true, result);

    }

    @Test
    @DisplayName("구독 취소")
    void cancelSubscribe() {

        // given
        MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .email("test@gmail.com")
            .memberGradeEntity(MemberGradeEntity.builder()
                .id(1L)
                .name("BASIC")
                .rewardPointPct(0).build())
            .memberStatus(MemberStatus.IN_USE)
            .build();
        MemberGradeEntity memberGrade = MemberGradeEntity.builder()
            .id(1L)
            .member(member)
            .name("BASIC")
            .rewardPointPct(1)
            .build();

        given(memberRepository.findByEmail(member.getEmail()))
            .willReturn(Optional.of(member));

        // when
        boolean result = subscribeService.cancel(member.getEmail(),
            memberGrade.getId());

        // then
        Assertions.assertEquals(true, result);

    }
}