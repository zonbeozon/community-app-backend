package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;

import java.util.*;

public class MarketTestDataProvideRouter {
    private final Map<Exchange, MarketTestDataProvideHandler> marketTestDataProvider;

    public MarketTestDataProvideRouter(MarketTestDataProvideHandler.MarketTestDataProvideHandlerSupplier marketTestDataProviderSupplier) {
        marketTestDataProvider = new HashMap<>();
        marketTestDataProvider.put(Exchange.UPBIT, marketTestDataProviderSupplier.get(Exchange.UPBIT, "/test-upbit-market.json"));
        marketTestDataProvider.put(Exchange.BINANCE, marketTestDataProviderSupplier.get(Exchange.BINANCE, "/test-binance-market.json"));
    }

    public Set<String> getMarketCodes(Exchange exchange) {
        return findMatchedProvider(exchange).getMarketCodes();
    }

    public List<Market> getMarkets(Exchange exchange) {
        return findMatchedProvider(exchange).getMarkets();
    }

    private MarketTestDataProvideHandler findMatchedProvider(Exchange exchange) {
        return Optional.ofNullable(marketTestDataProvider.get(exchange))
                .orElseThrow(() -> new IllegalArgumentException(exchange + "에 대한 TestDataProvideHandler가 등록되지 않았습니다."));
    }

    public static MarketTestDataProvideRouter withBasicHandler() {
        return new MarketTestDataProvideRouter(BasicMarketTestDataProvider::new);
    }

    public static MarketTestDataProvideRouter withFullHandler() {
        return new MarketTestDataProvideRouter(FullMarketTestDataProvider::new);
    }
}
