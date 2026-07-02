package com.example.tsubuyaki.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashtagTextTest {

    @Test
    @DisplayName("タグ抽出_nullは空リストを返す")
    void タグ抽出_nullは空リストを返す() {
        assertThat(HashtagText.extractTagNames(null)).isEmpty();
    }

    @Test
    @DisplayName("タグ抽出_空白のみは空リストを返す")
    void タグ抽出_空白のみは空リストを返す() {
        assertThat(HashtagText.extractTagNames(" 　\t")).isEmpty();
    }

    @Test
    @DisplayName("タグ抽出_タグなし本文は空リストを返す")
    void タグ抽出_タグなし本文は空リストを返す() {
        assertThat(HashtagText.extractTagNames("タグはありません")).isEmpty();
    }

    @Test
    @DisplayName("タグ抽出_同じタグは出現順で重複排除する")
    void タグ抽出_同じタグは出現順で重複排除する() {
        assertThat(HashtagText.extractTagNames("#Java #Spring #Java #社内_2026"))
                .containsExactly("Java", "Spring", "社内_2026");
    }

    @Test
    @DisplayName("タグ除去_nullは空文字を返す")
    void タグ除去_nullは空文字を返す() {
        assertThat(HashtagText.removeTags(null)).isEmpty();
    }

    @Test
    @DisplayName("タグ除去_空白のみは空文字を返す")
    void タグ除去_空白のみは空文字を返す() {
        assertThat(HashtagText.removeTags(" 　\t")).isEmpty();
    }

    @Test
    @DisplayName("タグ除去_タグだけの行は除外し本文行は空白正規化する")
    void タグ除去_タグだけの行は除外し本文行は空白正規化する() {
        String actual = HashtagText.removeTags("""
                  今日の　共有です\t#Java
                #Spring
                  次の行です
                """);

        assertThat(actual).isEqualTo("""
                今日の 共有です
                次の行です""");
    }
}
