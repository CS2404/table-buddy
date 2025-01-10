package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    public static final List<String> PERMIT_ALL_PATHS  = Arrays.asList(
            "/api/v1/auth/**"
    );

    private final MemberService memberService;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(PERMIT_ALL_PATHS.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )

                // 로그인 필터
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // JWT 토큰 인증 필터
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login"); // 로그인 URL
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public CustomAuthSuccessHandler customLoginSuccessHandler() {
        return new CustomAuthSuccessHandler(memberService, jwtUtils);
    }

    @Bean
    public CustomAuthFailureHandler customLoginFailureHandler() {
        return new CustomAuthFailureHandler();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(memberService, jwtUtils);
    }
}
