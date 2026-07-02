package com.example.tsubuyaki.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:postflow;MODE=Oracle;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=true"
})
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class PostFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("投稿導線_投稿作成後_一覧に作成した投稿を表示する")
    void 投稿導線_投稿作成後_一覧に作成した投稿を表示する() throws Exception {
        mockMvc.perform(post("/posts")
                        .with(csrf())
                        .param("author", "alice")
                        .param("avatarColor", "ORANGE")
                        .param("body", "通常投稿の反映を確認します"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("alice")))
                .andExpect(content().string(containsString("通常投稿の反映を確認します")))
                .andExpect(content().string(containsString("post__avatar-color--orange")))
                .andExpect(content().string(containsString("♥ 0")));
    }
}
