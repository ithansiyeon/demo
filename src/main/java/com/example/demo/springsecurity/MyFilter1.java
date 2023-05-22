package com.example.demo.springsecurity;

import com.example.demo.entity.Member;
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
        Member m = new Member("aaaa");
        String result = ms.getMessage("hello",null,null);
        memberService.join(m);
        memberService.findOne(1L);
        filterChain.doFilter(servletRequest, servletResponse);  // 다음 필터로 넘어가라는 의미
    }
}
