package com.cs2404.tablebuddy.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs2404.tablebuddy.common.config.security.JwtUtils;
import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberDto;
import com.cs2404.tablebuddy.member.entity.MemberAccessTokenEntity;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRefreshTokenEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
import com.cs2404.tablebuddy.member.repository.MemberRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtils jwtUtils;

    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() {
        // given
        MemberEntity member = getCustomerMemberEntity();

        // when
        memberService.signUp(
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getPhoneNumber(),
                member.getRole()
        );

        // then
        // MemberRepository를 Mock 객체로 만들어, saveMember() 메서드가 실제로 호출되었는지만 검증
        verify(memberRepository).saveMember(any(MemberEntity.class));
    }

    @Test
    @DisplayName("이메일 중복 검증 테스트")
    void validateEmailTest() {
        // given
        String email = "duplicate@test.com";
        MemberEntity member = Mockito.mock(MemberEntity.class);
        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(member));

        // when then
        CustomBusinessException exception = assertThrows(CustomBusinessException.class, () -> memberService.validateEmail(email));
        assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());

        verify(memberRepository).findMemberByEmail(email);
    }

    @Test
    @DisplayName("회원 ID로 회원 조회 테스트")
    void findMemberByMemberIdTest() {
        // given
        Long memberId = 1L;
        MemberEntity memberEntity = getCustomerMemberEntity();
        when(memberRepository.findMemberByMemberId(memberId)).thenReturn(Optional.of(memberEntity));

        // when
        MemberDto findMemberDto = memberService.findMemberByMemberId(memberId);

        // then
        assertNotNull(findMemberDto);
        assertEquals(memberEntity.getEmail(), findMemberDto.getEmail());
        verify(memberRepository).findMemberByMemberId(memberId);
    }

    @Test
    @DisplayName("회원 이메일로 회원 조회 테스트")
    void findMemberByEmailTest() {
        // given
        String email = "test@test.com";
        MemberEntity memberEntity = getCustomerMemberEntity();
        when(memberRepository.findMemberByEmail(email)).thenReturn(Optional.of(memberEntity));

        // when
        MemberDto memberDto = memberService.findMemberByEmail(email);

        // then
        assertNotNull(memberDto);
        assertEquals(memberEntity.getEmail(), memberDto.getEmail());
        verify(memberRepository).findMemberByEmail(email);
    }

    @Test
    @DisplayName("토큰 정보 저장 테스트")
    void saveTokenInfoTest() {
        // given
        Long memberId = 1L;
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";

        MemberEntity mockMember = getCustomerMemberEntity();
        Mockito.when(memberRepository.findMemberByMemberId(memberId)).thenReturn(Optional.of(mockMember));

        DecodedJWT mockDecodedAccessToken = Mockito.mock(DecodedJWT.class);
        DecodedJWT mockDecodedRefreshToken = Mockito.mock(DecodedJWT.class);

        Mockito.when(jwtUtils.decodeAccessToken(accessToken)).thenReturn(mockDecodedAccessToken);
        Mockito.when(jwtUtils.decodeRefreshToken(refreshToken)).thenReturn(mockDecodedRefreshToken);
        Mockito.when(jwtUtils.getExpireDateTimeFromDecodedToken(mockDecodedAccessToken)).thenReturn(LocalDateTime.now().plusHours(1));
        Mockito.when(jwtUtils.getExpireDateTimeFromDecodedToken(mockDecodedRefreshToken)).thenReturn(LocalDateTime.now().plusDays(7));

        // when
        memberService.saveTokenInfo(memberId, accessToken, refreshToken);

        // then
        // 메서드 호출 검증
        Mockito.verify(memberRepository).findMemberByMemberId(memberId);
        Mockito.verify(memberRepository).saveMemberAccessTokenEntity(any(MemberAccessTokenEntity.class));
        Mockito.verify(memberRepository).saveMemberRefreshTokenEntity(any(MemberRefreshTokenEntity.class));

    }


    private MemberEntity getCustomerMemberEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(MemberRole.class, () -> MemberRole.CUSTOMER);
        EasyRandom customerRandom = new EasyRandom(customerParam);
        return customerRandom.nextObject(MemberEntity.class);
    }





}