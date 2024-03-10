package com.example.demo.config;

import com.example.demo.interceptor.UrlInterceptor;
import com.example.demo.springsecurity.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**", "/*.ico", "/images/**","/jquery/**","/js/**","/login", "/loginError", "/access-denied", "/error/**");
        registry.addInterceptor(new UrlInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**", "/*.ico", "/images/**","/jquery/**","/js/**","/login", "/loginError", "/access-denied", "/error/**");
    }
}
