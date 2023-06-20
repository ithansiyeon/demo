package com.example.demo.springsecurity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AutoLoginUtil {
    // 다른 사용자로 자동 로그인하는 메소드
    public static void autoLogin(HttpServletRequest request, UserDetails userDetails, String loginId) {
        // 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 인증 객체를 시큐리티 컨텍스트 홀더에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 세션에 인증 객체 저장
        System.out.println("loginId = " + loginId);
        request.getSession().setAttribute("loginId", loginId);
    }
}
