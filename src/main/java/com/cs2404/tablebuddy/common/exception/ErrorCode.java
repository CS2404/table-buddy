package com.cs2404.tablebuddy.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"TB1001", "잘못된 입력값입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "TB1002", "이미 가입된 이메일 주소입니다"),
    INVALID_ROLE(HttpStatus.CONFLICT, "TB1003", "잘못된 권한입니다."),

    LOGIN_FAIL(HttpStatus.CONFLICT, "TB1004", "로그인 실패"),
    MEMBER_NOT_FOUND(HttpStatus.CONFLICT, "TB1005", "회원을 찾을 수 없습니다."),
    TOKEN_EXPIRED(HttpStatus.CONFLICT, "TB1006", "토큰이 만료되었습니다."),
    TOKEN_IS_NOT_VALID(HttpStatus.CONFLICT, "TB1007", "유효하지 않은 토큰입니다."),

    RESERVATION_PERMISSION_ERROR(HttpStatus.CONFLICT, "TBS1001", "예약 권한"),
    RESERVATION_NOT_FOUND(HttpStatus.CONFLICT, "TBS1002", "예약을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.CONFLICT, "TBS1003", "가게를 찾을 수 없습니다."),

    INVALID_BUSINESS_DAY(HttpStatus.BAD_REQUEST, "TBJ1001", "영업일을 잘 못 입력하셨습니다."),
    ALREADY_EXIST_STORE(HttpStatus.CONFLICT, "TBJ1002", "이미 등록한 가게가 존재합니다."),
    STORE_OWNER_MISMATCH(HttpStatus.BAD_REQUEST, "TBJ1003", "가게 소유자가 아닙니다."),
    STORE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TBJ1004", "가게를 찾을 수 없습니다." )
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
