package com.cs2404.tablebuddy.jongkuk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String role;


    public void isValid() {
        // TODO: 검증 로직
    }
}
