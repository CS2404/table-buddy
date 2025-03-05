package com.cs2404.tablebuddy.common.config.security;

import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.common.entity.DeleteStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Slf4j
@Getter
public class UserDetailsDto implements UserDetails {

	private final MemberDto memberDto;
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public UserDetailsDto(MemberDto memberDto, Collection<? extends GrantedAuthority> authorities) {
		this.memberDto = memberDto;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return memberDto.getEmail();
	}

	@Override
	public String getPassword() {
		return memberDto.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return memberDto.getIsDeleted() == DeleteStatus.N;
	}

	@Override
	public boolean isAccountNonLocked() {
		return memberDto.getIsDeleted() == DeleteStatus.N;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return memberDto.getIsDeleted() == DeleteStatus.N;
	}

	@Override
	public boolean isEnabled() {
		return memberDto.getIsDeleted() == DeleteStatus.N;
	}


}
