package com.example.tsubuyaki.controller;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.service.PostService;
import com.example.tsubuyaki.service.PostDetail;
import com.example.tsubuyaki.web.dto.ApiPostCreateRequest;
import com.example.tsubuyaki.web.dto.ApiPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts API", description = "投稿のREST API")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "投稿一覧を取得する", description = "論理削除されていない投稿を新着順に最大50件返します。")
    public List<ApiPostResponse> list() {
        return postService.latestDetails().stream()
                .map(ApiPostResponse::from)
                .toList();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "投稿詳細を取得する", description = "論理削除されていない投稿をIDで取得します。")
    public ApiPostResponse detail(@PathVariable Long id) {
        return ApiPostResponse.from(postService.getDetail(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "投稿を作成する", description = "投稿者名、アバター色、本文を指定して投稿を作成します。")
    public ResponseEntity<ApiPostResponse> create(@Valid @RequestBody ApiPostCreateRequest request) {
        Post post = postService.create(request.author(), request.avatarColor(), request.body());
        URI location = URI.create("/api/posts/" + post.getId());
        return ResponseEntity.created(location)
                .body(ApiPostResponse.from(new PostDetail(post, 0L)));
    }
}
