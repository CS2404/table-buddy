
package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.dto.MemberLoginDto;
import com.cs2404.tablebuddy.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthSuccessHandler(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Collection<? extends GrantedAuthority> authorities = ((UserDetailsDto) authentication.getPrincipal()).getAuthorities();
        MemberDto memberDto = ((UserDetailsDto) authentication.getPrincipal()).getMemberDto();

        try {
            String accessTokenValue = jwtUtils.generateAccessToken(memberDto);
            String refreshTokenValue = jwtUtils.generateRefreshToken(memberDto);


            memberService.saveTokenInfo(memberDto.getId(), accessTokenValue, refreshTokenValue);

            // 생성한 토큰 응답
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            MemberLoginDto.Response loginResponse = MemberLoginDto.Response.builder()
                    .accessToken(accessTokenValue)
                    .refreshToken(refreshTokenValue)
                    .build();

            objectMapper.writeValue(response.getWriter(), loginResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
