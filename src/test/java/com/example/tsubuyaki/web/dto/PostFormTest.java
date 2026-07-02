package com.example.tsubuyaki.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PostFormTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("投稿フォーム_投稿者アバター色本文_設定した値を取得できる")
    void 投稿フォーム_投稿者アバター色本文_設定した値を取得できる() {
        PostForm form = new PostForm();

        form.setAuthor("alice");
        form.setAvatarColor("GREEN");
        form.setBody("hello");

        assertThat(form.getAuthor()).isEqualTo("alice");
        assertThat(form.getAvatarColor()).isEqualTo("GREEN");
        assertThat(form.getBody()).isEqualTo("hello");
    }

    @Test
    @DisplayName("投稿フォーム_初期値_avatarColorはBLUE")
    void 投稿フォーム_初期値_avatarColorはBLUE() {
        PostForm form = new PostForm();

        assertThat(form.getAvatarColor()).isEqualTo("BLUE");
    }

    @Test
    @DisplayName("投稿フォーム選択肢_avatarColorOptions_値表示名CSSクラスを返す")
    void 投稿フォーム選択肢_avatarColorOptions_値表示名CSSクラスを返す() {
        PostFormOptions options = new PostFormOptions();

        assertThat(options.avatarColorOptions())
                .extracting(
                        PostFormOptions.AvatarColorOption::value,
                        PostFormOptions.AvatarColorOption::displayName,
                        PostFormOptions.AvatarColorOption::cssClass)
                .contains(
                        org.assertj.core.api.Assertions.tuple("BLUE", "青", "post__avatar-color--blue"),
                        org.assertj.core.api.Assertions.tuple("ORANGE", "オレンジ", "post__avatar-color--orange"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "　　"})
    @DisplayName("投稿フォーム_author_null空文字空白のみ_ConstraintViolationを返す")
    void 投稿フォーム_author_null空文字空白のみ_ConstraintViolationを返す(String author) {
        PostForm form = validForm();
        form.setAuthor(author);

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("author");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 30})
    @DisplayName("投稿フォーム_author_1文字と30文字_違反なし")
    void 投稿フォーム_author_1文字と30文字_違反なし(int length) {
        PostForm form = validForm();
        form.setAuthor("a".repeat(length));

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("投稿フォーム_author_31文字_ConstraintViolationを返す")
    void 投稿フォーム_author_31文字_ConstraintViolationを返す() {
        PostForm form = validForm();
        form.setAuthor("a".repeat(31));

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("author");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "　　"})
    @DisplayName("投稿フォーム_body_null空文字空白のみ_ConstraintViolationを返す")
    void 投稿フォーム_body_null空文字空白のみ_ConstraintViolationを返す(String body) {
        PostForm form = validForm();
        form.setBody(body);

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("body");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 280})
    @DisplayName("投稿フォーム_body_1文字と280文字_違反なし")
    void 投稿フォーム_body_1文字と280文字_違反なし(int length) {
        PostForm form = validForm();
        form.setBody("b".repeat(length));

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("投稿フォーム_body_281文字_ConstraintViolationを返す")
    void 投稿フォーム_body_281文字_ConstraintViolationを返す() {
        PostForm form = validForm();
        form.setBody("b".repeat(281));

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("body");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "BLACK"})
    @DisplayName("投稿フォーム_avatarColor_null空文字不正値_ConstraintViolationを返す")
    void 投稿フォーム_avatarColor_null空文字不正値_ConstraintViolationを返す(String avatarColor) {
        PostForm form = validForm();
        form.setAvatarColor(avatarColor);

        Set<ConstraintViolation<PostForm>> violations = validator.validate(form);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("avatarColor");
    }

    private static PostForm validForm() {
        PostForm form = new PostForm();
        form.setAuthor("alice");
        form.setAvatarColor("BLUE");
        form.setBody("hello");
        return form;
    }
}
