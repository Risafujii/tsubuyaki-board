package com.example.tsubuyaki.controller;

import com.example.tsubuyaki.config.SecurityConfig;
import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.service.PostDetail;
import com.example.tsubuyaki.service.PostNotFoundException;
import com.example.tsubuyaki.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiPostController.class)
@Import(SecurityConfig.class)
class ApiPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("投稿API_GET_api_posts_JSONで投稿一覧を返す")
    void 投稿API_GET_api_posts_JSONで投稿一覧を返す() throws Exception {
        given(postService.latestDetails()).willReturn(List.of(
                new PostDetail(
                        Post.reconstruct(1L, "alice", "BLUE", "API の共有です",
                                Instant.parse("2026-06-26T09:00:00Z")),
                        3L),
                new PostDetail(
                        Post.reconstruct(2L, "bob", "GREEN", "2件目です",
                                Instant.parse("2026-06-26T08:00:00Z")),
                        0L)));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].author").value("alice"))
                .andExpect(jsonPath("$[0].avatarColor").value("BLUE"))
                .andExpect(jsonPath("$[0].body").value("API の共有です"))
                .andExpect(jsonPath("$[0].createdAt").value("2026-06-26T09:00:00Z"))
                .andExpect(jsonPath("$[0].likeCount").value(3L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].author").value("bob"))
                .andExpect(jsonPath("$[1].avatarColor").value("GREEN"))
                .andExpect(jsonPath("$[1].body").value("2件目です"))
                .andExpect(jsonPath("$[1].createdAt").value("2026-06-26T08:00:00Z"))
                .andExpect(jsonPath("$[1].likeCount").value(0L));
    }

    @Test
    @DisplayName("投稿API_GET_api_posts_id_JSONで投稿詳細を返す")
    void 投稿API_GET_api_posts_id_JSONで投稿詳細を返す() throws Exception {
        given(postService.getDetail(1L)).willReturn(new PostDetail(
                Post.reconstruct(1L, "alice", "PURPLE", "詳細 API です",
                        Instant.parse("2026-06-26T09:00:00Z")),
                7L));

        mockMvc.perform(get("/api/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.author").value("alice"))
                .andExpect(jsonPath("$.avatarColor").value("PURPLE"))
                .andExpect(jsonPath("$.body").value("詳細 API です"))
                .andExpect(jsonPath("$.createdAt").value("2026-06-26T09:00:00Z"))
                .andExpect(jsonPath("$.likeCount").value(7L));
    }

    @Test
    @DisplayName("投稿API_GET_api_posts_id_存在しないidは404を返す")
    void 投稿API_GET_api_posts_id_存在しないidは404を返す() throws Exception {
        given(postService.getDetail(999L)).willThrow(new PostNotFoundException(999L));

        mockMvc.perform(get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("投稿が見つかりません"))
                .andExpect(jsonPath("$.path").value("/api/posts/999"));
    }

    @Test
    @DisplayName("投稿API_POST_api_posts_JSONで投稿を作成し201を返す")
    void 投稿API_POST_api_posts_JSONで投稿を作成し201を返す() throws Exception {
        given(postService.create("alice", "ORANGE", "API から投稿します")).willReturn(
                Post.reconstruct(10L, "alice", "ORANGE", "API から投稿します",
                        Instant.parse("2026-06-26T09:00:00Z")));

        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": "alice",
                                  "avatarColor": "ORANGE",
                                  "body": "API から投稿します"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/posts/10"))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.author").value("alice"))
                .andExpect(jsonPath("$.avatarColor").value("ORANGE"))
                .andExpect(jsonPath("$.body").value("API から投稿します"))
                .andExpect(jsonPath("$.likeCount").value(0L));

        verify(postService).create("alice", "ORANGE", "API から投稿します");
    }

    @Test
    @DisplayName("投稿API_POST_api_posts_バリデーションエラーは400を返す")
    void 投稿API_POST_api_posts_バリデーションエラーは400を返す() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": " ",
                                  "avatarColor": "BLUE",
                                  "body": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("入力内容を確認してください"))
                .andExpect(jsonPath("$.path").value("/api/posts"))
                .andExpect(jsonPath("$.fieldErrors.author").value("投稿者名を入力してください"))
                .andExpect(jsonPath("$.fieldErrors.body").value("本文を入力してください"));
    }

    @Test
    @DisplayName("投稿API_POST_api_posts_avatarColor不正は400を返す")
    void 投稿API_POST_api_posts_avatarColor不正は400を返す() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": "alice",
                                  "avatarColor": "BLACK",
                                  "body": "本文です"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("入力内容を確認してください"))
                .andExpect(jsonPath("$.path").value("/api/posts"))
                .andExpect(jsonPath("$.fieldErrors.avatarColor").value("アバター色を選択してください"));
    }

    @Test
    @DisplayName("投稿API_POST_api_posts_JSON不正は400を返す")
    void 投稿API_POST_api_posts_JSON不正は400を返す() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": "alice",
                                  "avatarColor": "BLUE",
                                  "body": "本文です"
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("リクエストJSONを確認してください"))
                .andExpect(jsonPath("$.path").value("/api/posts"));
    }

    @Test
    @DisplayName("投稿API_POST_api_posts_ContentType不正は415を返す")
    void 投稿API_POST_api_posts_ContentType不正は415を返す() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("plain text"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.error").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.message").value("Content-Type は application/json を指定してください"))
                .andExpect(jsonPath("$.path").value("/api/posts"));
    }
}
