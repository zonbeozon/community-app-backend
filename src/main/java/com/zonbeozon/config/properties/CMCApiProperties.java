package com.zonbeozon.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.api.cmc")
public record CMCApiProperties(
        String baseUrl,
        Paths paths,
        Headers headers,
        Params params
) {
    public record Paths(
            String metadata,
            String ticker
    ) {}

    public record Headers(
            String authKey,
            String authValue
    ) {}

    public record Params(
            String symbolKey,
            String auxKey,
            String tickerAuxValue,
            String metadataAuxValue
    ) {}
}
