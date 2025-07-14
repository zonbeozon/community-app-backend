package com.zonbeozon;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTestUtils {
    public static void assertNotAuthenticated(MockMvc mockMvc, HttpMethod httpMethod, String uri) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(httpMethod, uri))
                .andExpect(status().isUnauthorized());
    }
}
