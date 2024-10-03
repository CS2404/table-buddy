package com.cs2404.tablebuddy.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private List<String> messages;
    private String code;

    private ErrorResponse(final ErrorCode code) {
        this.messages = Collections.singletonList(code.getMessage());
        this.code = code.getCode();
    }

    public ErrorResponse(final ErrorCode code, final String message) {
        this.messages = Collections.singletonList(message);
        this.code = code.getCode();
    }

    public ErrorResponse(final ErrorCode code, final List<String> messages) {
        this.messages = messages;
        this.code = code.getCode();
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