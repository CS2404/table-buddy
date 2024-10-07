package com.cs2404.tablebuddy.member.service;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;
import com.cs2404.tablebuddy.member.dto.MemberSignUpDto;
import com.cs2404.tablebuddy.member.entity.DeleteStatus;
import com.cs2404.tablebuddy.member.entity.MemberEntity;
import com.cs2404.tablebuddy.member.entity.MemberRole;
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

    @Transactional
    public Long signUp(MemberSignUpDto.Request memberSignUpRequest) {
        validateEmail(memberSignUpRequest.getEmail());

        MemberEntity newMember = MemberEntity.builder()
                .name(memberSignUpRequest.getName())
                .phoneNumber(memberSignUpRequest.getPhoneNumber())
                .email(memberSignUpRequest.getEmail())
                .password(passwordEncoder.encode(memberSignUpRequest.getPassword()))
                .role(MemberRole.from(memberSignUpRequest.getRole()))
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
}
