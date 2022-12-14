package com.teamzero.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.teamzero.member.application.SignUpApplication;
import com.teamzero.member.domain.model.AdminEntity;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.model.constants.AdminStatus;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.AdminInfoDto;
import com.teamzero.member.domain.model.dto.MemberInfoDto;
import com.teamzero.member.domain.model.dto.ModifyDto;
import com.teamzero.member.domain.model.dto.SignUpDto;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private AdminService adminService;
    @InjectMocks
    private SignUpApplication signUpApplication;

    @Test
    @DisplayName("일반 회원 목록 조회")
    void getMemberList() {

        // given
        Pageable pageable = PageRequest.of(0, 10);
        MemberEntity member1 = MemberEntity.builder()
                .memberId(1L)
                    .email("test1@gmail.com")
                        .age(1)
                            .nickname("a")
                                .password("1234")
            .memberGradeEntity(new MemberGradeEntity())
        .build();
        MemberEntity member2 = MemberEntity.builder()
            .memberId(2L)
            .email("test2@gmail.com")
            .age(1)
            .nickname("a")
            .password("1234")
            .memberGradeEntity(new MemberGradeEntity())
            .build();
        MemberEntity member3 = MemberEntity.builder()
            .memberId(1L)
            .email("test3@gmail.com")
            .age(1)
            .nickname("a")
            .password("1234")
            .memberGradeEntity(new MemberGradeEntity())
            .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        given(memberRepository.findAll((Pageable) any()))
                .willReturn(getTestMemberPage(pageable));

        // when
        Page<MemberInfoDto> result = adminService.getMemberList(pageable);

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
        MemberInfoDto memberInfo = adminService.getMemberDetail(1L);

        // then
        Assertions.assertEquals(1L, memberInfo.getMemberId());
        Assertions.assertEquals("test@gmail.com", memberInfo.getEmail());
        Assertions.assertEquals(MemberStatus.IN_USE, MemberStatus.valueOf(memberInfo.getStatus()));

    }

    @Test
    @DisplayName("일반 회원 등급 및 상태 변경")
    void modifyMemberGradeOrStatus() {

        // given
        ModifyDto modify = ModifyDto.builder()
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
        MemberInfoDto memberInfo = adminService.modifyMemberGradeOrStatus(modify);

        // then
        Assertions.assertEquals(1L, memberInfo.getMemberId());
        Assertions.assertEquals("test@gmail.com", memberInfo.getEmail());
        Assertions.assertEquals("BASIC", memberInfo.getGrade());
        Assertions.assertEquals(MemberStatus.STOPPED, MemberStatus.valueOf(memberInfo.getStatus()));

    }

    @Test
    @DisplayName("관리자 회원 가입")
    void adminRegister() {

        // given
        SignUpDto.Request signUp = new SignUpDto.Request();
        signUp.setEmail("test@gmail.com");
        signUp.setPassword("test1");

        // when
        AdminInfoDto adminInfo = signUpApplication.registerAdmin(signUp);

        // then
        Assertions.assertEquals("test@gmail.com", adminInfo.getEmail());
        Assertions.assertEquals(MemberStatus.IN_USE, MemberStatus.valueOf(adminInfo.getStatus()));

    }

    @Test
    @DisplayName("관리자 상태 변경")
    void modifyAdminStatus() {

        // given
        ModifyDto modify = ModifyDto.builder()
            .memberId(1L)
            .status(String.valueOf(AdminStatus.STOPPED))
            .build();

        AdminEntity admin = AdminEntity.builder()
            .adminId(1L)
            .email("test@gmail.com")
            .adminStatus(AdminStatus.IN_USE)
            .build();


        given(adminRepository.findById(anyLong()))
            .willReturn(Optional.of(admin));

        // when
        AdminInfoDto adminInfo = adminService.modifyAdminStatus(modify);

        // then
        Assertions.assertEquals(1L, adminInfo.getAdminId());
        Assertions.assertEquals("test@gmail.com", adminInfo.getEmail());
        Assertions.assertEquals(AdminStatus.STOPPED, AdminStatus.valueOf(adminInfo.getStatus()));

    }


    private Page<MemberEntity> getTestMemberPage(Pageable pageable) {

        List<MemberEntity> list = new ArrayList();

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .build();
            list.add(member);
        }

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .build();
            list.add(member);
        }

        for (int i = 0; i < 4; i++) {
            MemberEntity member = MemberEntity.builder()
                    .memberId(i + 1L)
                    .email(String.format("test%d@gmail.com", i + 1))
                    .nickname("테스트")
                    .build();
            list.add(member);
        }

        return new PageImpl(list, pageable, list.size());
    }
}