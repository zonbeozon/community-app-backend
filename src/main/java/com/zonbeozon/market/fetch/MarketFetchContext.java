package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class MarketFetchContext {
    private final Map<Exchange, Set<String>> exchangeMarketCodesMap = new HashMap<>();

    public void put(Exchange exchange, Set<String> marketCodes) {
        exchangeMarketCodesMap.put(exchange, marketCodes);
    }
}
