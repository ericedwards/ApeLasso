package io.github.ericedwards.ApeLasso;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiKeyControllerTests {

    @Value("${application.security.initialApiKey}")
    private String apiKey;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createApiKeyAndTry() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/protected/apiKey")
                        .header("X-API-KEY", apiKey)
                        .param("apiKeyName", "anewkey"))
                .andExpect(status().isOk())
                .andReturn();
        String newApiKey = result.getResponse().getContentAsString();
        this.mockMvc.perform(get("/protected/greetings").header("X-API-KEY", newApiKey))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings")));
    }
}
