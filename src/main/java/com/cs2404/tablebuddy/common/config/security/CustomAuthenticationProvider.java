package com.cs2404.tablebuddy.common.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        try {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            String email = token.getName();
            String password = (String) token.getCredentials();

            UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, userDetailsDto.getPassword())) {
                throw new BadCredentialsException("password not match");
            }

            return new UsernamePasswordAuthenticationToken(
                    userDetailsDto,
                    null,
                    userDetailsDto.getAuthorities()
            );

        } catch (Exception e) {
            throw new BadCredentialsException("로그인 실패");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}