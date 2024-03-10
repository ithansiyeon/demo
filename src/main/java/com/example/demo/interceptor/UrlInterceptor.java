package com.example.demo.interceptor;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.springsecurity.SecurityUser;
import com.example.demo.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@RequiredArgsConstructor
public class UrlInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullPathName = "";
        boolean isExist = false;
        if (principal instanceof SecurityUser userDetails) {
            List<MenuResultDto> menuList = userDetails.getMenuList();
            String url = request.getRequestURI();
            loop:
            for (MenuResultDto depth1 : menuList) {
                String menu1 = depth1.getMenuName();
                if(!StringUtil.isEmpty(depth1.getAuthority()) && url.startsWith(depth1.getAuthority())) {
                    fullPathName = depth1.getMenuName();
                    isExist = true;
                    break;
                }
                for (MenuResultDto depth2 : depth1.getChildren()) {
                    fullPathName = menu1 + " > "+depth2.getMenuName();
                    if(!StringUtil.isEmpty(depth2.getAuthority()) && url.startsWith(depth2.getAuthority())) {
                        isExist = true;
                        break loop;
                    }
                    for (MenuResultDto depth3 : depth2.getChildren()) {
                        if(!StringUtil.isEmpty(depth3.getAuthority()) && url.startsWith(depth3.getAuthority())) {
                            fullPathName+=" > "+depth3.getMenuName();
                            isExist = true;
                            break loop;
                        }
                    }
                }
            }
        }
        HttpSession session = request.getSession();
        if (isExist) {
            session.setAttribute("fullPathName",fullPathName);
        } else {
            session.setAttribute("fullPathName","");
        }
        return true;
    }
}