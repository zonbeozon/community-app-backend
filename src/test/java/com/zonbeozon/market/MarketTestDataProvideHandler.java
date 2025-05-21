package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;

import java.util.List;
import java.util.Set;

public interface MarketTestDataProvideHandler {
    Set<String> getMarketCodes();
    List<Market> getMarkets();

    @FunctionalInterface
    interface MarketTestDataProvideHandlerSupplier {
        MarketTestDataProvideHandler get(Exchange exchange, String dataPath);
    }
}
