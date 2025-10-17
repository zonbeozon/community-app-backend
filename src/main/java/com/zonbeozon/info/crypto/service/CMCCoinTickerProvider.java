package com.zonbeozon.info.crypto.service;

import com.zonbeozon.config.properties.CMCApiProperties;
import com.zonbeozon.global.fetch.FetchException;
import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.dto.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CMCCoinTickerProvider implements CoinTickerProvider {
    private final RestClient restClient;
    private final CMCApiProperties cmcApiProperties;

    public CMCCoinTickerProvider(CMCApiProperties cmcApiProperties, RestClient.Builder restClientBuilder) {
        this.cmcApiProperties = cmcApiProperties;
        this.restClient = restClientBuilder
                .baseUrl(cmcApiProperties.baseUrl())
                .defaultHeader(cmcApiProperties.headers().authKey(), cmcApiProperties.headers().authValue())
                .build();
    }

    @Override
    public List<CoinTickerDto> provide(Collection<String> symbols) {
        String joinedSymbol = String.join(",", symbols);
        CMCTickerResponse response = Optional.ofNullable(restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(cmcApiProperties.paths().ticker())
                        .queryParam(cmcApiProperties.params().symbolKey(), joinedSymbol)
                        .queryParam(cmcApiProperties.params().auxKey(), cmcApiProperties.params().tickerAuxValue())
                        .build())
                .retrieve()
                .body(CMCTickerResponse.class)
        ).orElseThrow(() -> new FetchException("response body is null"));

        return response.tickerMap().values().stream().map(ticker -> new CoinTickerDto(
                ticker.symbol(),
                ticker.rank(),
                ticker.circulatingSupply(),
                ticker.totalSupply(),
                Map.of(BaseAsset.USD, new CoinQuoteDto(
                        ticker.quote().quoteUsd().marketCap(),
                        ticker.quote().quoteUsd().fullyDilutedMarketCap(),
                        ticker.quote().quoteUsd().volume24h()
                )),
                OffsetDateTime.parse(ticker.timestamp()).toLocalDateTime()
        )).toList();
    }
}
