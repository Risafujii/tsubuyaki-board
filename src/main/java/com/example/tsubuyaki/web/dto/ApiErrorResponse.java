package com.example.tsubuyaki.web.dto;

import java.util.Map;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors) {

    public ApiErrorResponse {
        fieldErrors = Map.copyOf(fieldErrors);
    }

    public static ApiErrorResponse notFound(String path) {
        return new ApiErrorResponse(
                404,
                "Not Found",
                "投稿が見つかりません",
                path,
                Map.of());
    }

    public static ApiErrorResponse badRequest(String path, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(
                400,
                "Bad Request",
                "入力内容を確認してください",
                path,
                fieldErrors);
    }
}
