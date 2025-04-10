package com.cs2404.tablebuddy;

import com.cs2404.tablebuddy.common.config.security.LoginMemberArgumentResolver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용
        return http.build();
    }

    @Bean
    public HandlerMethodArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver();
    }
}