package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

interface MarketFetcher {
    MarketFetchResult fetch(Set<String> marketCodes);
    CompletableFuture<MarketFetchResult> fetchAsync(Set<String> marketCodes);
    boolean isSupportedExchange(Exchange exchange);
}
