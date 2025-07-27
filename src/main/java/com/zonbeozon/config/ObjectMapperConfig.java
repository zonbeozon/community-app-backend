package com.zonbeozon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.global.ObjectResponseToByteConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class ObjectMapperConfig {

    private static final String INTERNAL_SERVER_ERROR_PAYLOAD =
                        """
                        {
                            "code":"INTERNAL_SERVER_ERROR",
                            "message":"알수 없는 에러 발생."
                        }
                        """;
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectResponseToByteConverter objectToStringConverter() {
        return (Object object) -> {
            try {
                return objectMapper().writeValueAsBytes(object);
            } catch (IOException e) {
                log.error("직렬화 실패: {}", e.getMessage());
                return INTERNAL_SERVER_ERROR_PAYLOAD.getBytes(StandardCharsets.UTF_8);
            }
        };

    }
}
