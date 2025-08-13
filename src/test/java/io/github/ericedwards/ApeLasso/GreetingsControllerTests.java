package io.github.ericedwards.ApeLasso;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingsControllerTests {

    @Value("${application.security.initialApiKey}")
    private String apiKey;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetPublicGreetings() throws Exception {
        this.mockMvc.perform(get("/public/greetings"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings")));
    }

    @Test
    void shouldGetProtectedGreetings() throws Exception {
        this.mockMvc.perform(get("/protected/greetings").header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings")));
    }

    @Test
    void shouldReturnError_IfWrongApiKeyIsProvided_WhenAccessingProtectedGreetings() throws Exception {
        this.mockMvc.perform(get("/protected/greetings").header("X-API-KEY", "mywrongkey"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnError_IfNoApiKeyIsProvided_WhenAccessingProtectedGreetings() throws Exception {
        this.mockMvc.perform(get("/protected/greetings"))
                .andExpect(status().isUnauthorized());
    }

}
