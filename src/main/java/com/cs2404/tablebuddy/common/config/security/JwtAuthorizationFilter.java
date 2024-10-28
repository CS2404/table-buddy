package com.cs2404.tablebuddy.common.config.security;


import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> publicUrlPatterns = Arrays.asList(
            "/api/v1/auth/**"
    );

    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final AntPathMatcher pathMatcher  = new AntPathMatcher();

    public JwtAuthorizationFilter(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws IOException, ServletException {

        // OPTIONS 요청
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            chain.doFilter(request, response);
            return;
        }

        // public 경로
        if (publicUrlPatterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()))) {
            chain.doFilter(request, response);
            return;
        }

        Long memberId = getMemberIdFromAccessToken(request);
        MemberDto member = memberService.findMemberByMemberId(memberId);
        saveMemberToSecurityContextHolder(member);

        chain.doFilter(request, response);
    }

    public Long getMemberIdFromAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtUtils.TOKEN_AUTH_HEADER_NAME);
        if (!StringUtils.hasText(authHeader)) {
            throw new CustomBusinessException(ErrorCode.INVALID_ROLE);
        }

        String accessToken = authHeader.split(JwtUtils.AUTH_HEADER_TOKEN_DELIMITER)[1];

        return jwtUtils.getMemberIdFromDecodedToken(accessToken);
    }

    /**
     * securityContext(ThreadLocal 내부)에 회원 정보 저장
     */
    private static void saveMemberToSecurityContextHolder(MemberDto memberDto) {
        Collection<? extends GrantedAuthority> authorities = memberDto.getAuthorities();

        UserDetailsDto userDetailsDto = new UserDetailsDto(memberDto, authorities);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetailsDto,
                null,
                userDetailsDto.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
