package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean hasMemberInfoType = MemberDto.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginMemberAnnotation && hasMemberInfoType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) throws Exception {

        // 현재 인증된 사용자 정보를 SecurityContextHolder 에서 조회
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            // 인증되지 않은 경우 null 반환
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsDto) {
            return ((UserDetailsDto) principal).getMemberDto();
        }

        return null;
//        throw new IllegalStateException("인증된 사용자 정보가 유효하지 않음");
    }
}
