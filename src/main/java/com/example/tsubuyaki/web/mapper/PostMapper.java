package com.example.tsubuyaki.web.mapper;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.service.PostDetail;
import com.example.tsubuyaki.web.dto.PostResponse;

import java.util.List;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getAuthor(),
                post.getAvatarColor(),
                post.getBody(),
                post.getDisplayBody(),
                post.getCreatedAt(),
                post.getTagNames());
    }

    public static PostResponse toResponse(PostDetail detail) {
        Post post = detail.post();
        return new PostResponse(
                post.getId(),
                post.getAuthor(),
                post.getAvatarColor(),
                post.getBody(),
                post.getDisplayBody(),
                post.getCreatedAt(),
                post.getTagNames(),
                detail.likeCount());
    }

    public static List<PostResponse> toResponseList(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    public static List<PostResponse> toDetailResponseList(List<PostDetail> details) {
        return details.stream()
                .map(PostMapper::toResponse)
                .toList();
    }
}
