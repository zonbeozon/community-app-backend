package com.zonbeozon.info.crypto.service;

import com.zonbeozon.config.properties.CMCApiProperties;
import com.zonbeozon.info.crypto.domain.LanguageCode;
import com.zonbeozon.global.fetch.FetchException;
import com.zonbeozon.info.crypto.dto.CMCMetadataResponse;
import com.zonbeozon.info.crypto.dto.CoinMetadataDto;
import com.zonbeozon.info.crypto.dto.LocalizedCoinInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.*;

@Component
public class CMCCoinMetadataProvider implements CoinMetadataProvider {
    private final RestClient restClient;
    private final CMCApiProperties cmcApiProperties;

    public CMCCoinMetadataProvider(RestClient.Builder restClientBuilder, CMCApiProperties cmcApiProperties) {
        this.restClient = restClientBuilder
                .baseUrl(cmcApiProperties.baseUrl())
                .defaultHeader(cmcApiProperties.headers().authKey(), cmcApiProperties.headers().authValue())
                .build();
        this.cmcApiProperties = cmcApiProperties;
    }

    @Override
    public List<CoinMetadataDto> provide(Collection<String> symbols) {
        String joinedSymbol = String.join(",", symbols);
        CMCMetadataResponse response = Optional.ofNullable(restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(cmcApiProperties.paths().metadata())
                        .queryParam(cmcApiProperties.params().symbolKey(), joinedSymbol)
                        .queryParam(cmcApiProperties.params().auxKey(), cmcApiProperties.params().metadataAuxValue())
                        .build())
                .retrieve()
                .body(CMCMetadataResponse.class)
        ).orElseThrow(() -> new FetchException("response body is null"));

        return response.metadataMap().values().stream().map(metadata -> {
            String website = metadata.urls().websites().isEmpty() ? null : metadata.urls().websites().getFirst();
            return new CoinMetadataDto(
                    metadata.symbol(),
                    metadata.logo(),
                    Map.of(LanguageCode.EN, new LocalizedCoinInfoDto(metadata.name(), metadata.description())),
                    website,
                    OffsetDateTime.parse(response.status().timestamp()).toLocalDateTime()
            );
        }).toList();
    }
}
