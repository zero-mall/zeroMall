package com.teamzero.member.service;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.MemberInfo;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("일반 회원 목록 조회")
    void getMemberList() {

        // given
        Pageable pageable = PageRequest.of(0, 10);

        given(memberRepository.findAll((Pageable) any()))
                .willReturn(getTestMemberPage(pageable));

        // when
        Page<MemberInfo> result = adminService.getMemberList(pageable);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test1@gmail.com", result.getContent().get(0).getEmail());
        Assertions.assertEquals("test2@gmail.com", result.getContent().get(1).getEmail());
        Assertions.assertEquals("test3@gmail.com", result.getContent().get(2).getEmail());

    }

    @Test
    @DisplayName("일반 회원 상세 조회")
    void getMemberDetail() {

        // given
        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .email("test@gmail.com")
                .memberGradeEntity(MemberGradeEntity.builder().id(1L).name("BASIC").rewardPointPct(0).build())
                .memberStatus(MemberStatus.IN_USE)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        // when
        MemberInfo memberInfo = adminService.getMemberDetail(1L);

        // then
        Assertions.assertEquals(1L, memberInfo.getMemberId());
        Assertions.assertEquals("test@gmail.com", memberInfo.getEmail());
        Assertions.assertEquals(MemberStatus.IN_USE, MemberStatus.valueOf(memberInfo.getStatus()));

    }

    @Test
    @DisplayName("일반 회원 등급 및 상태 변경")
    void updateMemberGradeOrStatus() {

        // given
        Modify modify = Modify.builder()
                .memberId(1L)
                .grade("BASIC")
                .status(String.valueOf(MemberStatus.STOPPED))
                .build();

        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .email("test@gmail.com")
                .memberGradeEntity(MemberGradeEntity.builder().id(1L).name("BASIC").rewardPointPct(0).build())
                .memberStatus(MemberStatus.IN_USE)
                .build();

        MemberGradeEntity memberGrade = MemberGradeEntity.builder()
                .id(1L)
                .name("BASIC")
                .rewardPointPct(0)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(memberGradeRepository.findByName(anyString()))
                .willReturn(Optional.of(memberGrade));

        // when
        MemberInfo memberInfo = adminService.updateMemberGradeOrStatus(modify);

        // then
        Assertions.assertEquals(1L, memberInfo.getMemberId());
        Assertions.assertEquals("test@gmail.com", memberInfo.getEmail());
        Assertions.assertEquals("BASIC", memberInfo.getGrade());
        Assertions.assertEquals(MemberStatus.STOPPED, MemberStatus.valueOf(memberInfo.getStatus()));

    }

     private Page<MemberEntity> getTestMemberPage(Pageable pageable) {

        List<MemberEntity> list = new ArrayList();

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .memberGradeEntity(new MemberGradeEntity(i + 1L, "BASIC", 0))
                    .build();
            list.add(member);
        }

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .memberGradeEntity(new MemberGradeEntity(i + 1L, "STANDARD", 2))
                    .build();
            list.add(member);
        }

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .memberGradeEntity(new MemberGradeEntity(i + 1L, "PREMIUM", 3))
                    .build();
            list.add(member);
        }

        return new PageImpl(list, pageable, list.size());
    }
}