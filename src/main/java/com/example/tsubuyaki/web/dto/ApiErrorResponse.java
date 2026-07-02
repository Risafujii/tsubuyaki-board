package com.example.tsubuyaki.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "APIエラーレスポンス")
public record ApiErrorResponse(
        @Schema(description = "HTTPステータス", example = "400")
        int status,

        @Schema(description = "エラー種別", example = "Bad Request")
        String error,

        @Schema(description = "エラーメッセージ", example = "入力内容を確認してください")
        String message,

        @Schema(description = "リクエストパス", example = "/api/posts")
        String path,

        @Schema(description = "フィールド別エラー")
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

    public static ApiErrorResponse malformedJson(String path) {
        return new ApiErrorResponse(
                400,
                "Bad Request",
                "リクエストJSONを確認してください",
                path,
                Map.of());
    }

    public static ApiErrorResponse unsupportedMediaType(String path) {
        return new ApiErrorResponse(
                415,
                "Unsupported Media Type",
                "Content-Type は application/json を指定してください",
                path,
                Map.of());
    }
}
