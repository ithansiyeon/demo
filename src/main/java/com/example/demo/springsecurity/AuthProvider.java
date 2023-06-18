package com.example.demo.springsecurity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.example.demo.utils.CoreUtil.getClientIP;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName(); // 인증 요청의 principal을 가져옴
        String password = authentication.getCredentials().toString(); // 인증 요청의 credentials를 가져옴
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getClientIP(request);
        // UserDetailsService를 사용하여 사용자 정보 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

        PasswordEncoder passwordEncoder = userDetailsService.getPasswordEncoder();

        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            userDetailsService.insertUserLog(ip, true, loginId);
            userDetailsService.updateLastLoginDate(loginId);
            HttpSession session = request.getSession();
            session.setAttribute("userName",userDetails.getUsername());
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            userDetailsService.insertUserLog(ip, false, loginId);
        }

        throw new BadCredentialsException("No such user or wrong password.");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
