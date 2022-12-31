package com.teamzero.member.filter;

import com.teamzero.domain.JwtAuthenticationProvider;
import com.teamzero.domain.domain.UserVo;
import com.teamzero.member.service.MemberService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/member/*")
@RequiredArgsConstructor
public class MemberFilter implements Filter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");

        if (!jwtAuthenticationProvider.validToken(token)) {
            throw new ServletException("Invalid Access");
        }

        UserVo vo = jwtAuthenticationProvider.getUserVo(token);

        memberService.findByMemberIdAndEmail(vo.getMemberId(), vo.getEmail());

        chain.doFilter(request, response);

    }
}
