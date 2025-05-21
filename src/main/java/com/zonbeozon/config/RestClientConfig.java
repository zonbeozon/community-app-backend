package com.zonbeozon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    @Scope("prototype")
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
