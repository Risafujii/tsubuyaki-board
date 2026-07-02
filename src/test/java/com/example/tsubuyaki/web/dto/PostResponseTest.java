package com.example.tsubuyaki.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostResponseTest {

    @Test
    @DisplayName("PostResponse_簡易コンストラクタ_avatarColor既定値と表示本文を設定する")
    void PostResponse_簡易コンストラクタ_avatarColor既定値と表示本文を設定する() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "hello #Java",
                Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(response.avatarColor()).isEqualTo("BLUE");
        assertThat(response.displayBody()).isEqualTo("hello");
        assertThat(response.tagNames()).isEmpty();
    }

    @Test
    @DisplayName("PostResponse_色指定コンストラクタ_タグなしで表示本文を設定する")
    void PostResponse_色指定コンストラクタ_タグなしで表示本文を設定する() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "GREEN",
                "hello #Java",
                Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(response.avatarColor()).isEqualTo("GREEN");
        assertThat(response.displayBody()).isEqualTo("hello");
        assertThat(response.tagNames()).isEmpty();
    }

    @Test
    @DisplayName("PostResponse_タグ付きコンストラクタ_表示本文とタグを設定する")
    void PostResponse_タグ付きコンストラクタ_表示本文とタグを設定する() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "ORANGE",
                "hello #Java",
                Instant.parse("2026-06-26T09:00:00Z"),
                List.of("Java"));

        assertThat(response.displayBody()).isEqualTo("hello");
        assertThat(response.tagNames()).containsExactly("Java");
    }

    @Test
    @DisplayName("PostResponse_件数付きコンストラクタ_likeCountを設定する")
    void PostResponse_件数付きコンストラクタ_likeCountを設定する() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "ORANGE",
                "hello #Java",
                "hello",
                Instant.parse("2026-06-26T09:00:00Z"),
                List.of("Java"),
                3L);

        assertThat(response.likeCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("PostResponse_既存コンストラクタ_likeCountは0にする")
    void PostResponse_既存コンストラクタ_likeCountは0にする() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "hello",
                Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(response.likeCount()).isZero();
    }

    @Test
    @DisplayName("PostResponse_compactConstructor_displayBody_nullは空文字にする")
    void PostResponse_compactConstructor_displayBody_nullは空文字にする() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "BLUE",
                "hello",
                null,
                Instant.parse("2026-06-26T09:00:00Z"),
                List.of());

        assertThat(response.displayBody()).isEmpty();
    }

    @Test
    @DisplayName("PostResponse_compactConstructor_tagNamesは防御的コピーする")
    void PostResponse_compactConstructor_tagNamesは防御的コピーする() {
        List<String> tags = new java.util.ArrayList<>(List.of("Java"));
        PostResponse response = new PostResponse(
                1L,
                "alice",
                "BLUE",
                "hello",
                "hello",
                Instant.parse("2026-06-26T09:00:00Z"),
                tags);

        tags.add("Spring");

        assertThat(response.tagNames()).containsExactly("Java");
        assertThatThrownBy(() -> response.tagNames().add("Spring"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("PostResponse_avatarColorCssClass_色名を小文字CSSクラスにする")
    void PostResponse_avatarColorCssClass_色名を小文字CSSクラスにする() {
        PostResponse response = new PostResponse(
                1L,
                "alice",
                " PURPLE ",
                "hello",
                Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(response.avatarColorCssClass()).isEqualTo("post__avatar-color--purple");
    }
}
