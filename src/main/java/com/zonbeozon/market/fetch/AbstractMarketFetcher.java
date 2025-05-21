package com.zonbeozon.market.fetch;

import com.zonbeozon.common.fetch.FetchException;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.fiat.entity.FiatType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

abstract class AbstractMarketFetcher<T extends MarketFetchResponse> implements MarketFetcher {
    private final RestClient restClient;
    private final ParameterizedTypeReference<List<T>> responseType;

    public AbstractMarketFetcher(RestClient.Builder restClientBuilder, ParameterizedTypeReference<List<T>> responseType) {
        this.restClient = restClientBuilder.build();
        this.responseType = responseType;
    }

    @Override
    public MarketFetchResult fetch(Set<String> marketCodes) {
        List<T> body = Optional.ofNullable(restClient.get()
                        .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(getBaseUrl())
                        .path(getResourceUrl())
                        .queryParams(getMarketCodeParams(marketCodes))
                        .build())
                        .retrieve()
                        .body(responseType)
                ).orElseThrow(() -> new FetchException("body is null"));

        List<MarketFetchData> results = body.stream().map(marketFetchResponse -> marketFetchResponse.getResult(getFiatType())).toList();
        return new MarketFetchResult(getExchange(), results);
    }

    @Override
    @Async
    public CompletableFuture<MarketFetchResult> fetchAsync(Set<String> marketCodes) {
        return CompletableFuture.completedFuture(fetch(marketCodes));
    }

    abstract protected Exchange getExchange();
    abstract protected FiatType getFiatType();
    abstract protected String getBaseUrl();
    abstract protected String getResourceUrl();
    abstract protected MultiValueMap<String, String> getMarketCodeParams(Set<String> marketCodes);

    @Override
    public boolean isSupportedExchange(Exchange exchange) {
        return getExchange() == exchange;
    }
}

