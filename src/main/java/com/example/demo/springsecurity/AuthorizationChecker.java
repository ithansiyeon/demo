package com.example.demo.springsecurity;

import com.example.demo.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorizationChecker {
    //private final Logger log = LoggerFactory.getLogger(this.getClass());

    // 캐싱처리 적용
    @Cacheable("check")
    public boolean check(HttpServletRequest request, Authentication authentication) throws Exception{
        Object principalObj = authentication.getPrincipal();

        // 시큐리티 사용자 정보가 AdminDTO인 경우만 허용
        if (!(principalObj instanceof UserDto)) {
            return false;
        }

       /* List<String> authority = new ArrayList<String>();
        // Initializer에서 저장한 menu static list를 이용한 메뉴-권한 조회
        for (SecurityUrlMatcher matcher : MenuStaticValue.menuList) {
            log.info("[AuthorizationChecker] matcher url ==> " + matcher.getUrl() + ", ajax url ==> " + matcher.getAjaxUrl() + ", matcher authority ==> " + matcher.getAuthority());
            log.info("[AuthorizationChecker] request url ==> " + request.getRequestURI());
            // API 비동기 요청인 경우 AJAX URL 체크
            if("true".equals(request.getHeader("AJAX"))) {
                if (matcher.getAjaxUrl() != null && new AntPathMatcher().match(matcher.getAjaxUrl(), request.getRequestURI())) {
                    authority.add(matcher.getAuthority());
                }
            }
            // 그 외 요청인 경우 URL 체크
            else {
                if (new AntPathMatcher().match(matcher.getUrl(), request.getRequestURI())) {
                    authority.add(matcher.getAuthority());
                }
            }
        }

        String adminId = ((AdminDTO) authentication.getPrincipal()).getAdmId();
        AdminDTO aDTO = new AdminDTO();
        aDTO.setAdmId(adminId);
        List<AdminDTO> authorities = // ... DB에서 해당 사용자의 권한 조회
        if(authorities == null || authorities.size() == 0) {
            return false;
        }

        log.info("[AuthorizationChecker] admin authority ==> " + authority);
        log.info("[AuthorizationChecker] admin authorities ==> " + authorities);
        for(AdminDTO auth : authorities) {
            if (authority == null || !authority.contains(auth.getRoleNm())) {
                return false;
            }
        }*/

        return true;
    }
}
