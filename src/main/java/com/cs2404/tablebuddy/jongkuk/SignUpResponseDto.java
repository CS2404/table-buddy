package com.cs2404.tablebuddy.jongkuk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpResponseDto {

    private Long id;

    public SignUpResponseDto(Long id) {
        this.id = id;
    }
}
