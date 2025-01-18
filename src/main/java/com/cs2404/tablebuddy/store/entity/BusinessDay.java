package com.cs2404.tablebuddy.store.entity;

import com.cs2404.tablebuddy.common.exception.CustomBusinessException;
import com.cs2404.tablebuddy.common.exception.ErrorCode;

public enum BusinessDay {
    월요일, 화요일, 수요일, 목요일, 금요일, 토요일, 일요일;

    /*
    valueOf 메소드는 문자열을 기반으로 열거형 상수를 찾고 반환하는 정적 메서드로
    정확히 일치하는 열거형 상수 이름이 있는 경우 해당 상수를 반환하며,
    이름이 일치하지 않거나 잘못된 경우에는 IllegalArgumentException 예외를 던짐.
     */
    public static BusinessDay fromString(String value) {
        try {
            return BusinessDay.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new CustomBusinessException(ErrorCode.INVALID_BUSINESS_DAY, "잘못된 영업요일을 입력하셨습니다. " + "입력된 값: " + value);
        }
    }
}
