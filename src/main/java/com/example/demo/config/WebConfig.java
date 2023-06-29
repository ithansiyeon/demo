package com.example.demo.config;

import com.example.demo.interceptor.UrlInterceptor;
import com.example.demo.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MenuService menuService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UrlInterceptor(menuService))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**", "/*.ico", "/images/**","/jquery/**","/js/**","/login", "/loginError", "/access-denied", "/error/**");
    }
}
