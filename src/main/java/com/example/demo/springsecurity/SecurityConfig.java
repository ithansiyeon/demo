package com.example.demo.springsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private final CustomAuthFailureHandler customAuthFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(new MyFilter1(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/fragments/**","/login", "/loginError","/css/**","/images/**","/jquery/**","/js/**","/index", "/board", "/menu/authorityMenuList").permitAll()
                        .anyRequest().authenticated())
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .usernameParameter("loginId")
                .passwordParameter("loginPassword")
                .defaultSuccessUrl("/board")
                .failureHandler(customAuthFailureHandler)
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me")
                .and()
                .exceptionHandling().accessDeniedPage("/login")
                .and()
                .rememberMe()
                .key("12345678")
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(3600*24*365)
                .and()
                .build();
    }
}