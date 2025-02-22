package com.cs2404.tablebuddy;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class TestUtils {

    // Reflection을 활용해 테스트용도로 강제로 변경
    public static void setCreatedAt(Object entity, LocalDateTime createdAt) {
        try {
            Field field = entity.getClass().getSuperclass().getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(entity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set createdAt field", e);
        }
    }
}
