package com.example.demo.interceptor;

import com.example.demo.springsecurity.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

public class UrlInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println("apple");
        if(matcher.match(securityUser.getUrl(), requestURI)) {
            return true;
        }
        response.sendRedirect("/access-denied");
        return false; //false 진행X
    }
}