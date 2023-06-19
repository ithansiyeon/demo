package com.example.demo.config;

import com.example.demo.interceptor.UrlInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UrlInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**", "/*.ico", "/images/**","/jquery/**","/js/**","/login","/access-denied", "/error/**");
    }
}
