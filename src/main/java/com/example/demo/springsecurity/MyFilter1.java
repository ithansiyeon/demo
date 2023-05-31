package com.example.demo.springsecurity;

import com.example.demo.service.MemberService;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.io.IOException;

@RequiredArgsConstructor
public class MyFilter1 implements Filter {

    private final MessageSource ms;

    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);  // 다음 필터로 넘어가라는 의미
    }
}
