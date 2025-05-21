package com.zonbeozon.market.fetch;

import com.zonbeozon.common.fetch.FetchContextSupplier;
import com.zonbeozon.common.fetch.FetchManager;
import com.zonbeozon.exchange.Exchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
class MarketFetchManager implements FetchManager<MarketFetchContext, MarketFetchResultWrapper> {
    private final Set<MarketFetcher> fetchers;

    @Override
    public MarketFetchResultWrapper fetch(FetchContextSupplier<MarketFetchContext> contextSupplier) {
        List<CompletableFuture<MarketFetchResult>> cfResults = contextSupplier.getContext().getExchangeMarketCodesMap().entrySet().stream()
                .map(entry -> fetchAsync(entry.getKey(), entry.getValue()))
                .toList();
        return new MarketFetchResultWrapper(cfResults.stream().map(CompletableFuture::join).toList());
    }

    private CompletableFuture<MarketFetchResult> fetchAsync(Exchange exchange, Set<String> marketCodes) {
        MarketFetcher matched = fetchers.stream().filter(fetcher -> fetcher.isSupportedExchange(exchange)).findAny()
                .orElseThrow(() -> new IllegalStateException(exchange + "를 지원하는 fetcher가 존재하지 않습니다."));
        return matched.fetchAsync(marketCodes);
    }
}
