package com.teamzero.member.controller;

import com.teamzero.member.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @MockBean
    private AdminService adminService;

    @Test
    void getMemberList() {
    }

    @Test
    @DisplayName("일반 회원 상세 조회")
    void getMemberDetail() {



    }

    @Test
    @DisplayName("일반 회원 등급 및 상태 변경")
    void modifyMemberGradeOrStatus() {
    }
}