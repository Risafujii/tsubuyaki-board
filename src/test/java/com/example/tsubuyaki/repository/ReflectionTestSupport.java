package com.example.tsubuyaki.repository;

import java.lang.reflect.Field;

final class ReflectionTestSupport {

    private ReflectionTestSupport() {
    }

    static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("テスト用フィールド設定に失敗しました: " + fieldName, e);
        }
    }
}
