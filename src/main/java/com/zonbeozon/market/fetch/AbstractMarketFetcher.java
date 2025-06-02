package com.zonbeozon.market.fetch;

import com.zonbeozon.common.fetch.FetchException;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.entity.Market;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;

abstract class AbstractMarketFetcher<T extends MarketFetchResponse> implements MarketFetcher {
    private final RestClient restClient;
    private final ParameterizedTypeReference<List<T>> responseType;
    private final MarketFetchDataMapper<T> mapper = new MarketFetchDataMapper<>();

    public AbstractMarketFetcher(RestClient.Builder restClientBuilder, ParameterizedTypeReference<List<T>> responseType) {
        this.restClient = restClientBuilder.build();
        this.responseType = responseType;
    }

    public List<MarketFetchData> fetch(MarketHolder marketHolder) {
        List<T> body = Optional.ofNullable(restClient.get()
                        .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(getBaseUrl())
                        .path(getResourceUrl())
                        .queryParams(convertMarketToParam(marketHolder))
                        .build())
                        .retrieve()
                        .body(responseType)
                ).orElseThrow(() -> new FetchException("body is null"));
        return mapper.map(marketHolder, body, getFiatType());
    }

    @Override
    @Async
    public CompletableFuture<List<MarketFetchData>> fetchAsync(MarketHolder marketHolder) {
        return CompletableFuture.completedFuture(fetch(marketHolder));
    }

    abstract protected Exchange getExchange();
    abstract protected FiatType getFiatType();
    abstract protected String getBaseUrl();
    abstract protected String getResourceUrl();
    abstract protected MultiValueMap<String, String> convertMarketToParam(MarketHolder marketHolder);

    @Override
    public boolean isSupportedExchange(Exchange exchange) {
        return getExchange() == exchange;
    }
}

