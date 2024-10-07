package com.cs2404.tablebuddy.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"TB1001", "잘못된 입력값입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "TB1002", "이미 가입된 이메일 주소입니다"),
    INVALID_ROLE(HttpStatus.CONFLICT, "TB1003", "잘못된 권한입니다.")
    ;

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
