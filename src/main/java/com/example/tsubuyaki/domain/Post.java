package com.example.tsubuyaki.domain;

import java.time.Instant;
import java.util.List;

public final class Post {

    private static final int AUTHOR_MAX_LENGTH = 30;
    private static final int BODY_MAX_LENGTH = 280;

    private final Long id;

    private final String author;

    private final String avatarColor;

    private final String body;

    private final Instant createdAt;

    private final List<String> tagNames;

    private Instant deletedAt;

    private Post(
            Long id,
            String author,
            String avatarColor,
            String body,
            Instant createdAt,
            List<String> tagNames,
            Instant deletedAt) {
        this.id = id;
        this.author = author;
        this.avatarColor = avatarColor;
        this.body = body;
        this.createdAt = createdAt;
        this.tagNames = tagNames;
        this.deletedAt = deletedAt;
    }

    public static Post create(String author, String body, Instant createdAt) {
        return create(author, AvatarColor.DEFAULT.name(), body, createdAt);
    }

    public static Post create(String author, String avatarColor, String body, Instant createdAt) {
        return reconstruct(null, author, avatarColor, body, createdAt, List.of(), null);
    }

    public static Post reconstruct(Long id, String author, String body, Instant createdAt) {
        return reconstruct(id, author, AvatarColor.DEFAULT.name(), body, createdAt, List.of(), null);
    }

    public static Post reconstruct(Long id, String author, String avatarColor, String body, Instant createdAt) {
        return reconstruct(id, author, avatarColor, body, createdAt, List.of(), null);
    }

    public static Post reconstruct(
            Long id,
            String author,
            String avatarColor,
            String body,
            Instant createdAt,
            List<String> tagNames) {
        return reconstruct(id, author, avatarColor, body, createdAt, tagNames, null);
    }

    public static Post reconstruct(
            Long id,
            String author,
            String avatarColor,
            String body,
            Instant createdAt,
            List<String> tagNames,
            Instant deletedAt) {
        String normalizedAuthor = normalizeRequired(author, "author", AUTHOR_MAX_LENGTH);
        String normalizedAvatarColor = AvatarColor.from(avatarColor).name();
        String normalizedBody = normalizeRequired(body, "body", BODY_MAX_LENGTH);
        Instant normalizedCreatedAt = requireCreatedAt(createdAt);
        List<String> normalizedTagNames = copyTagNames(tagNames);
        return new Post(
                id,
                normalizedAuthor,
                normalizedAvatarColor,
                normalizedBody,
                normalizedCreatedAt,
                normalizedTagNames,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatarColor() {
        return avatarColor;
    }

    public String getBody() {
        return body;
    }

    public String getDisplayBody() {
        return HashtagText.removeTags(body);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted(Instant deletedAt) {
        if (deletedAt == null) {
            throw new IllegalArgumentException("deletedAt must not be null");
        }
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    private static String normalizeRequired(String value, String fieldName, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        String normalized = value.strip();
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must be " + maxLength + " characters or less");
        }
        return normalized;
    }

    private static Instant requireCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt must not be null");
        }
        return createdAt;
    }

    private static List<String> copyTagNames(List<String> tagNames) {
        if (tagNames == null) {
            throw new IllegalArgumentException("tagNames must not be null");
        }
        return List.copyOf(tagNames);
    }
}
