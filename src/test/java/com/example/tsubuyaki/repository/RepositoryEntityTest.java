package com.example.tsubuyaki.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepositoryEntityTest {

    @Test
    @DisplayName("PostEntity_削除状態_削除日時の有無で判定する")
    void PostEntity_削除状態_削除日時の有無で判定する() {
        PostEntity post = postEntity(1L);

        assertThat(post.isDeleted()).isFalse();

        post.markDeleted(Instant.parse("2026-06-26T10:00:00Z"));

        assertThat(post.getDeletedAt()).isEqualTo(Instant.parse("2026-06-26T10:00:00Z"));
        assertThat(post.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("PostEntity_markDeleted_nullは例外を投げる")
    void PostEntity_markDeleted_nullは例外を投げる() {
        PostEntity post = postEntity(1L);

        assertThatThrownBy(() -> post.markDeleted(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("deletedAt");
    }

    @Test
    @DisplayName("PostEntity_equals_同じ参照は等価")
    void PostEntity_equals_同じ参照は等価() {
        PostEntity post = postEntity(1L);

        assertThat(post).isEqualTo(post);
    }

    @Test
    @DisplayName("PostEntity_equals_型が違う値は等価ではない")
    void PostEntity_equals_型が違う値は等価ではない() {
        PostEntity post = postEntity(1L);

        assertThat(post).isNotEqualTo("post");
    }

    @Test
    @DisplayName("PostEntity_equals_idが同じなら等価")
    void PostEntity_equals_idが同じなら等価() {
        PostEntity first = postEntity(1L);
        PostEntity second = postEntity(1L);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("PostEntity_equals_idが異なるかnullなら等価ではない")
    void PostEntity_equals_idが異なるかnullなら等価ではない() {
        assertThat(postEntity(1L)).isNotEqualTo(postEntity(2L));
        assertThat(postEntity(null)).isNotEqualTo(postEntity(1L));
    }

    @Test
    @DisplayName("PostEntity_getTags_タグの防御的コピーを返す")
    void PostEntity_getTags_タグの防御的コピーを返す() {
        TagEntity java = new TagEntity(1L, "Java");
        PostEntity post = new PostEntity(
                1L,
                "alice",
                "BLUE",
                "body #Java",
                Instant.parse("2026-06-26T09:00:00Z"),
                List.of(java));

        assertThat(post.getTags()).containsExactly(java);
        assertThatThrownByUnsupportedOperation(() -> post.getTags().add(new TagEntity(2L, "Spring")));
    }

    @Test
    @DisplayName("TagEntity_equals_同じ参照と同じidは等価")
    void TagEntity_equals_同じ参照と同じidは等価() {
        TagEntity tag = new TagEntity(1L, "Java");

        assertThat(tag).isEqualTo(tag);
        assertThat(tag).isEqualTo(new TagEntity(1L, "Spring"));
        assertThat(tag.hashCode()).isEqualTo(new TagEntity(1L, "Java").hashCode());
    }

    @Test
    @DisplayName("TagEntity_equals_型違いとid違いとnull_idは等価ではない")
    void TagEntity_equals_型違いとid違いとnull_idは等価ではない() {
        assertThat(new TagEntity(1L, "Java")).isNotEqualTo("Java");
        assertThat(new TagEntity(1L, "Java")).isNotEqualTo(new TagEntity(2L, "Java"));
        assertThat(new TagEntity(null, "Java")).isNotEqualTo(new TagEntity(1L, "Java"));
    }

    @Test
    @DisplayName("LikeEntity_コンストラクタとgetter_設定値を返す")
    void LikeEntity_コンストラクタとgetter_設定値を返す() {
        LikeEntity like = new LikeEntity(10L, "abc12345");

        assertThat(like.getId()).isNull();
        assertThat(like.getPostId()).isEqualTo(10L);
        assertThat(like.getClientHash()).isEqualTo("abc12345");
    }

    @Test
    @DisplayName("LikeEntity_equals_同じ参照と同じidは等価")
    void LikeEntity_equals_同じ参照と同じidは等価() {
        LikeEntity like = likeEntityWithId(1L);

        assertThat(like).isEqualTo(like);
        assertThat(like).isEqualTo(likeEntityWithId(1L));
        assertThat(like.hashCode()).isEqualTo(likeEntityWithId(1L).hashCode());
    }

    @Test
    @DisplayName("LikeEntity_equals_型違いとid違いとnull_idは等価ではない")
    void LikeEntity_equals_型違いとid違いとnull_idは等価ではない() {
        assertThat(likeEntityWithId(1L)).isNotEqualTo("like");
        assertThat(likeEntityWithId(1L)).isNotEqualTo(likeEntityWithId(2L));
        assertThat(likeEntityWithId(null)).isNotEqualTo(likeEntityWithId(1L));
    }

    private static PostEntity postEntity(Long id) {
        return new PostEntity(id, "alice", "BLUE", "body", Instant.parse("2026-06-26T09:00:00Z"));
    }

    private static LikeEntity likeEntityWithId(Long id) {
        LikeEntity like = new LikeEntity(1L, "abc12345");
        ReflectionTestSupport.setField(like, "id", id);
        return like;
    }

    private static void assertThatThrownByUnsupportedOperation(Runnable runnable) {
        try {
            runnable.run();
        } catch (UnsupportedOperationException e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
            return;
        }
        throw new AssertionError("UnsupportedOperationException が発生しませんでした");
    }
}
