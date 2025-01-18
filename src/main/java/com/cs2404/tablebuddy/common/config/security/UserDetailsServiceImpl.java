package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberService memberService;

	public UserDetailsServiceImpl(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (!StringUtils.hasText(email)) {
			throw new AuthenticationServiceException("로그인 실패");
		}

		MemberDto memberDto = memberService.findMemberByEmail(email);

		return new UserDetailsDto(memberDto, memberDto.getAuthorities());
	}

}
