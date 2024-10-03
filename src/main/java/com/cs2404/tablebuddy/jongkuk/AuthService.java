package com.cs2404.tablebuddy.jongkuk;

import com.cs2404.tablebuddy.jongkuk.memberenum.DeleteStatus;
import com.cs2404.tablebuddy.jongkuk.memberenum.MemberRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // TODO: 트랜잭션 없이 db 에 저장됨.
//    @Transactional
    public Long signUp(SignUpRequestDto signUpRequestDto) {

        MemberEntity newMember = MemberEntity.builder()
                .name(signUpRequestDto.getName())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword()) // TODO: 암호화
                .role(MemberRole.from(signUpRequestDto.getRole()))
                .isDeleted(DeleteStatus.N)
                .build();

        return memberRepository.saveMember(newMember);
    }
}
