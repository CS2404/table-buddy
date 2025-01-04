package com.cs2404.tablebuddy.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs2404.tablebuddy.common.config.security.JwtUtils;
import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.dto.MemberSignUpDto;
import com.cs2404.tablebuddy.member.entity.*;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public Long signUp(String email,
                       String rawPassword,
                       String name,
                       String phoneNumber,
                       MemberRole memberRole
    ) {
        validateEmail(email);

        MemberEntity newMember = MemberEntity.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(memberRole)
                .isDeleted(DeleteStatus.N)
                .build();

        return memberRepository.saveMember(newMember);
    }

    public void validateEmail(String email) {
        memberRepository.findMemberByEmail(email)
                .ifPresent(member -> {
                    throw new CustomBusinessException(ErrorCode.DUPLICATE_EMAIL);
                });
    }

    public MemberDto findMemberByMemberId(Long memberId) {
        MemberEntity memberEntity = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return new MemberDto(memberEntity);
    }

    public MemberDto findMemberByEmail(String email) {
        MemberEntity memberEntity = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return new MemberDto(memberEntity);
    }

    @Transactional
    public void saveTokenInfo(Long memberId, String accessToken, String refreshToken) {
        MemberEntity member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.MEMBER_NOT_FOUND));

        DecodedJWT decodedAccessToken = jwtUtils.decodeAccessToken(accessToken);
        DecodedJWT decodedRefreshToken = jwtUtils.decodeRefreshToken(refreshToken);

        MemberAccessTokenEntity memberAccessTokenEntity = MemberAccessTokenEntity.builder()
                .accessToken(accessToken)
                .accessTokenExpiredAt(jwtUtils.getExpireDateTimeFromDecodedToken(decodedAccessToken))
                .memberEntity(member)
                .build();

        MemberRefreshTokenEntity memberRefreshTokenEntity = MemberRefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiredAt(jwtUtils.getExpireDateTimeFromDecodedToken(decodedRefreshToken))
                .memberEntity(member)
                .build();

        memberRepository.saveMemberAccessTokenEntity(memberAccessTokenEntity);
        memberRepository.saveMemberRefreshTokenEntity(memberRefreshTokenEntity);
    }

}
