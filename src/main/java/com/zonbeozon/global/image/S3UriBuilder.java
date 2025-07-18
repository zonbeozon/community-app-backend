package com.zonbeozon.global.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3UriBuilder {
    private final S3Properties properties;

    public String build(String key) {
        return String.format(properties.getEndpoint() + "/" + key);
    }
}
