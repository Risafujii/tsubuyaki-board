package com.example.tsubuyaki.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("APIドキュメント_SwaggerUIを表示できる")
    void APIドキュメント_SwaggerUIを表示できる() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Swagger UI")));
    }

    @Test
    @DisplayName("APIドキュメント_GET_api_postsがOpenAPIに含まれる")
    void APIドキュメント_GET_api_postsがOpenAPIに含まれる() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/posts'].get.summary").value("投稿一覧を取得する"));
    }

    @Test
    @DisplayName("APIドキュメント_投稿APIのエラー応答がOpenAPIに含まれる")
    void APIドキュメント_投稿APIのエラー応答がOpenAPIに含まれる() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/posts/{id}'].get.responses['404'].description")
                        .value("投稿が見つからない"))
                .andExpect(jsonPath("$.paths['/api/posts/{id}'].get.responses['404']"
                        + ".content['application/json'].schema['$ref']")
                        .value("#/components/schemas/ApiErrorResponse"))
                .andExpect(jsonPath("$.paths['/api/posts'].post.responses['400'].description")
                        .value("入力内容が不正"))
                .andExpect(jsonPath("$.paths['/api/posts'].post.responses['400']"
                        + ".content['application/json'].schema['$ref']")
                        .value("#/components/schemas/ApiErrorResponse"))
                .andExpect(jsonPath("$.paths['/api/posts'].post.responses['415'].description")
                        .value("Content-Typeが不正"))
                .andExpect(jsonPath("$.paths['/api/posts'].post.responses['415']"
                        + ".content['application/json'].schema['$ref']")
                        .value("#/components/schemas/ApiErrorResponse"));
    }
}
