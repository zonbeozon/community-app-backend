package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.SimpleMarketHolder;
import com.zonbeozon.market.entity.Market;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketFetchManager {
    private final Set<MarketFetcher> fetchers;

    public List<MarketFetchData> fetch(MarketHolder marketHolder) {
        List<CompletableFuture<List<MarketFetchData>>> cfResults = marketHolder.toExchangeMap().entrySet().stream()
                .map(entry -> fetchByExchange(entry.getKey(), entry.getValue()))
                .toList();
        return cfResults.stream().map(CompletableFuture::join).flatMap(List::stream).toList();
    }

    private CompletableFuture<List<MarketFetchData>> fetchByExchange(Exchange exchange, Set<Market> markets) {
        MarketFetcher matched = fetchers.stream().filter(fetcher -> fetcher.isSupportedExchange(exchange)).findAny()
                .orElseThrow(() -> new IllegalStateException(exchange + "를 지원하는 fetcher가 존재하지 않습니다."));
        return matched.fetchAsync(new SimpleMarketHolder(markets));
    }
}
