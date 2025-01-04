package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.dto.MemberLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberLoginDto.Request loginRequest = objectMapper
                    .readValue(request.getInputStream(), MemberLoginDto.Request.class);

            String email = loginRequest.getEmail(); // 로그인 아이디(이메일)
            String password = loginRequest.getPassword(); // 비밀번호

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    password
            );
            setDetails(request, authToken);

            Authentication authenticate = this.getAuthenticationManager().authenticate(authToken);

            return authenticate;

        } catch (Exception e) {
            log.error("Login Fail : {}", e.getMessage(), e);
            e.printStackTrace();

            throw new BadCredentialsException("로그인 실패");
        }
    }

}
