package com.cs2404.tablebuddy.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String code;
    private List<String> messages;

    private ErrorResponse(final ErrorCode code) {
        this.code = code.getCode();
        this.messages = Collections.singletonList(code.getMessage());
    }

    public ErrorResponse(final ErrorCode code, final String message) {
        this.code = code.getCode();
        this.messages = Collections.singletonList(message);
    }

    public ErrorResponse(final ErrorCode code, final List<String> messages) {
        this.code = code.getCode();
        this.messages = messages;
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<String> messages) {
        return new ErrorResponse(code, messages);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }
}