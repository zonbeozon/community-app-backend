package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.MarketHolder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

interface MarketFetcher {
    CompletableFuture<List<MarketFetchData>> fetchAsync(MarketHolder marketHolder);
    boolean isSupportedExchange(Exchange exchange);
}
