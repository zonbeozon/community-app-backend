package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MarketFetchTestDataProvideRouter {
    private final Map<Exchange, MarketFetchTestDataProvideHandler> marketFetchTestDataProvider;

    public MarketFetchTestDataProvideRouter() {
        marketFetchTestDataProvider = new HashMap<>();
        marketFetchTestDataProvider.put(Exchange.UPBIT, new FullMarketTestDataProvider(Exchange.UPBIT, "/test-upbit-market.json"));
        marketFetchTestDataProvider.put(Exchange.BINANCE, new FullMarketTestDataProvider(Exchange.BINANCE, "/test-binance-market.json"));
    }

    public MarketFetchResult getMarketFetchResult(Exchange exchange) {
        return findMatchedProvider(exchange).getMarketFetchResult();
    }

    public Set<String> getMarketCodes(Exchange exchange) {
        return findMatchedProvider(exchange).getMarketCodes();
    }

    private MarketFetchTestDataProvideHandler findMatchedProvider(Exchange exchange) {
        return Optional.ofNullable(marketFetchTestDataProvider.get(exchange))
                .orElseThrow(() -> new IllegalArgumentException(exchange + "에 대한 TestDataProvideHandler가 등록되지 않았습니다."));
    }
}
