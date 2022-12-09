package com.teamzero.member.service;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @InjectMocks
    private MemberService memberService;


}