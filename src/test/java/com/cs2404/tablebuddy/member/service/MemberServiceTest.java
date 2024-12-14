package com.cs2404.tablebuddy.member.service;

import com.cs2404.tablebuddy.common.config.security.JwtUtils;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
        Mockito.verify(memberRepository).saveMember(any(MemberEntity.class));
    }

    private MemberEntity getCustomerMemberEntity() {
        EasyRandomParameters customerParam = new EasyRandomParameters();
        customerParam.randomize(MemberRole.class, () -> MemberRole.CUSTOMER);
        EasyRandom customerRandom = new EasyRandom(customerParam);
        return customerRandom.nextObject(MemberEntity.class);
    }





}