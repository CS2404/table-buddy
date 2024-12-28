package com.cs2404.tablebuddy.common.exception;

import lombok.Getter;

@Getter
public class CustomBusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomBusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
