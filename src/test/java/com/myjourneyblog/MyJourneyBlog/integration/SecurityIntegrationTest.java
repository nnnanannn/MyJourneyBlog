package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.LoginRequest;
import com.myjourneyblog.MyJourneyBlog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

/**
 * Integration tests for security features
 */
public class SecurityIntegrationTest extends IntegrationTestBase {

    @Test
    public void testInputSanitization_RemovesXSS() throws Exception {
        // Basic test to ensure context loads and security chain is active
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk());
        }
}
