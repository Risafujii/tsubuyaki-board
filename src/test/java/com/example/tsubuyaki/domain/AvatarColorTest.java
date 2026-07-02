package com.example.tsubuyaki.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AvatarColorTest {

    @Test
    @DisplayName("AvatarColor_from_nullは既定色を返す")
    void AvatarColor_from_nullは既定色を返す() {
        assertThat(AvatarColor.from(null)).isEqualTo(AvatarColor.BLUE);
    }

    @Test
    @DisplayName("AvatarColor_from_空白は既定色を返す")
    void AvatarColor_from_空白は既定色を返す() {
        assertThat(AvatarColor.from("  ")).isEqualTo(AvatarColor.BLUE);
    }

    @Test
    @DisplayName("AvatarColor_from_前後空白を除いて色を返す")
    void AvatarColor_from_前後空白を除いて色を返す() {
        assertThat(AvatarColor.from(" RED ")).isEqualTo(AvatarColor.RED);
    }

    @Test
    @DisplayName("AvatarColor_from_不正値は例外を投げる")
    void AvatarColor_from_不正値は例外を投げる() {
        assertThatThrownBy(() -> AvatarColor.from("BLACK"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("BLACK");
    }

    @Test
    @DisplayName("AvatarColor_names_定義順の名前を返す")
    void AvatarColor_names_定義順の名前を返す() {
        assertThat(AvatarColor.names())
                .containsExactly("RED", "BLUE", "GREEN", "YELLOW", "PURPLE", "ORANGE");
    }

    @Test
    @DisplayName("AvatarColor_getDisplayName_日本語表示名を返す")
    void AvatarColor_getDisplayName_日本語表示名を返す() {
        assertThat(AvatarColor.ORANGE.getDisplayName()).isEqualTo("オレンジ");
    }
}
