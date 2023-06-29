package com.example.demo.interceptor;

import com.example.demo.service.menu.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class UrlInterceptor implements HandlerInterceptor {

    private final MenuService menuService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println("apple");
        System.out.println(requestURI);
        String byUrl = menuService.findByUrl(request.getRequestURI());
        HttpSession session = request.getSession();
        session.setAttribute("fullPathName", byUrl);
//        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        AntPathMatcher matcher = new AntPathMatcher();
       /* if(matcher.match(securityUser.getUrl(), requestURI)) {
            return true;
        }*/
//        if(requestURI.contains(securityUser.getUrl())) {
//            return true;
//        }
//        response.sendRedirect("/access-denied");
//        return false; //false 진행X
        return true;
    }
}