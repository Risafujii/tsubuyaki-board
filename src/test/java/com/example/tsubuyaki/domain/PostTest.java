package com.example.tsubuyaki.domain;

import jakarta.persistence.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {

    @Test
    @DisplayName("同一性_未永続の投稿同士_idがnullでも等価にしない")
    void 同一性_未永続の投稿同士_idがnullでも等価にしない() {
        Post first = Post.create("alice", "hello", Instant.parse("2026-06-26T09:00:00Z"));
        Post second = Post.create("alice", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("投稿作成_前後空白_正規化して保持する")
    void 投稿作成_前後空白_正規化して保持する() {
        Post post = Post.create(" alice ", " hello ", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getAuthor()).isEqualTo("alice");
        assertThat(post.getBody()).isEqualTo("hello");
    }

    @Test
    @DisplayName("表示用本文_本文にタグがあるとき_タグ文字列を除去する")
    void 表示用本文_本文にタグがあるとき_タグ文字列を除去する() {
        Post post = Post.create("alice", """
                今日の共有です #java #spring
                #社内勉強会
                次の行です
                """, Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getBody()).contains("#java", "#spring", "#社内勉強会");
        assertThat(post.getDisplayBody()).isEqualTo("""
                今日の共有です
                次の行です""");
    }

    @Test
    @DisplayName("論理削除_markDeleted_削除日時を保持し削除済み判定できる")
    void 論理削除_markDeleted_削除日時を保持し削除済み判定できる() {
        Post post = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.isDeleted()).isFalse();

        post.markDeleted(Instant.parse("2026-06-26T10:00:00Z"));

        assertThat(post.getDeletedAt()).isEqualTo(Instant.parse("2026-06-26T10:00:00Z"));
        assertThat(post.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("論理削除_markDeleted_nullは例外を投げる")
    void 論理削除_markDeleted_nullは例外を投げる() {
        Post post = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThatThrownBy(() -> post.markDeleted(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("deletedAt");
    }

    @Test
    @DisplayName("投稿作成_author空白_factoryが例外を投げる")
    void 投稿作成_author空白_factoryが例外を投げる() {
        assertThatThrownBy(() -> Post.create(" ", "hello", Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("author");
    }

    @Test
    @DisplayName("投稿作成_author_null_factoryが例外を投げる")
    void 投稿作成_author_null_factoryが例外を投げる() {
        assertThatThrownBy(() -> Post.create(null, "hello", Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("author");
    }

    @Test
    @DisplayName("投稿作成_body空白_factoryが例外を投げる")
    void 投稿作成_body空白_factoryが例外を投げる() {
        assertThatThrownBy(() -> Post.create("alice", "　 ", Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("body");
    }

    @Test
    @DisplayName("投稿作成_body_null_factoryが例外を投げる")
    void 投稿作成_body_null_factoryが例外を投げる() {
        assertThatThrownBy(() -> Post.create("alice", null, Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("body");
    }

    @Test
    @DisplayName("投稿作成_author30文字_body280文字_作成できる")
    void 投稿作成_author30文字_body280文字_作成できる() {
        Post post = Post.create("a".repeat(30), "b".repeat(280), Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getAuthor()).hasSize(30);
        assertThat(post.getBody()).hasSize(280);
    }

    @Test
    @DisplayName("投稿作成_author31文字_例外を投げる")
    void 投稿作成_author31文字_例外を投げる() {
        assertThatThrownBy(() -> Post.create("a".repeat(31), "hello", Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("30 characters or less");
    }

    @Test
    @DisplayName("投稿作成_body281文字_例外を投げる")
    void 投稿作成_body281文字_例外を投げる() {
        assertThatThrownBy(() -> Post.create("alice", "b".repeat(281), Instant.parse("2026-06-26T09:00:00Z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("280 characters or less");
    }

    @Test
    @DisplayName("投稿作成_createdAt未指定_factoryが例外を投げる")
    void 投稿作成_createdAt未指定_factoryが例外を投げる() {
        assertThatThrownBy(() -> Post.create("alice", "hello", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("createdAt");
    }

    @Test
    @DisplayName("投稿復元_avatarColor_null_既定色BLUEを使う")
    void 投稿復元_avatarColor_null_既定色BLUEを使う() {
        Post post = Post.reconstruct(1L, "alice", null, "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getAvatarColor()).isEqualTo("BLUE");
    }

    @Test
    @DisplayName("投稿復元_avatarColor空白_既定色BLUEを使う")
    void 投稿復元_avatarColor空白_既定色BLUEを使う() {
        Post post = Post.reconstruct(1L, "alice", "  ", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getAvatarColor()).isEqualTo("BLUE");
    }

    @Test
    @DisplayName("投稿復元_avatarColor前後空白_正規化して使う")
    void 投稿復元_avatarColor前後空白_正規化して使う() {
        Post post = Post.reconstruct(1L, "alice", " GREEN ", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post.getAvatarColor()).isEqualTo("GREEN");
    }

    @Test
    @DisplayName("投稿復元_tagNames_null_例外を投げる")
    void 投稿復元_tagNames_null_例外を投げる() {
        assertThatThrownBy(() -> Post.reconstruct(
                1L,
                "alice",
                "BLUE",
                "hello",
                Instant.parse("2026-06-26T09:00:00Z"),
                null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tagNames");
    }

    @Test
    @DisplayName("投稿復元_tagNames_防御的コピーを保持する")
    void 投稿復元_tagNames_防御的コピーを保持する() {
        List<String> tags = new java.util.ArrayList<>(List.of("Java"));
        Post post = Post.reconstruct(
                1L,
                "alice",
                "BLUE",
                "hello #Java",
                Instant.parse("2026-06-26T09:00:00Z"),
                tags);

        tags.add("Spring");

        assertThat(post.getTagNames()).containsExactly("Java");
        assertThatThrownBy(() -> post.getTagNames().add("Spring"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("同一性_equals_同じ参照は等価")
    void 同一性_equals_同じ参照は等価() {
        Post post = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post).isEqualTo(post);
    }

    @Test
    @DisplayName("同一性_equals_型が違う値は等価ではない")
    void 同一性_equals_型が違う値は等価ではない() {
        Post post = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(post).isNotEqualTo("post");
    }

    @Test
    @DisplayName("同一性_equals_idが同じなら等価")
    void 同一性_equals_idが同じなら等価() {
        Post first = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));
        Post second = Post.reconstruct(1L, "bob", "GREEN", "other", Instant.parse("2026-06-26T10:00:00Z"));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("同一性_equals_idが異なる投稿は等価ではない")
    void 同一性_equals_idが異なる投稿は等価ではない() {
        Post first = Post.reconstruct(1L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));
        Post second = Post.reconstruct(2L, "alice", "BLUE", "hello", Instant.parse("2026-06-26T09:00:00Z"));

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("責務分離_Domain_PostはJPA Entityではない")
    void 責務分離_Domain_PostはJPA_Entityではない() {
        assertThat(Post.class.getAnnotation(Entity.class)).isNull();
    }

    @Test
    @DisplayName("セキュリティ_Postは継承できない")
    void セキュリティ_Postは継承できない() {
        assertThat(Modifier.isFinal(Post.class.getModifiers())).isTrue();
    }
}
