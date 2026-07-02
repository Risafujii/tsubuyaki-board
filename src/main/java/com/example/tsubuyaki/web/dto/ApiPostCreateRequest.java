package com.example.tsubuyaki.web.dto;

import com.example.tsubuyaki.domain.AvatarColor;
import com.example.tsubuyaki.web.validation.NotBlankText;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "投稿作成APIのリクエスト")
public record ApiPostCreateRequest(
        @Schema(description = "投稿者名", example = "alice")
        @NotBlankText(message = "投稿者名を入力してください")
        @Size(max = 30, message = "投稿者名は 30 文字以内で入力してください")
        String author,

        @Schema(description = "アバター色", example = "BLUE")
        @NotBlank(message = "アバター色を選択してください")
        @Pattern(regexp = AvatarColor.PATTERN, message = "アバター色を選択してください")
        String avatarColor,

        @Schema(description = "本文", example = "API から投稿します")
        @NotBlankText(message = "本文を入力してください")
        @Size(max = 280, message = "本文は 280 文字以内で入力してください")
        String body) {
}
