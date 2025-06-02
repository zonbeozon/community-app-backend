package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimpleMarketHolder implements MarketHolder {
    private final Set<Market> markets;

    @Override
    public Map<Exchange, Set<Market>> toExchangeMap() {
        return markets.stream().collect(Collectors.groupingBy(Market::getExchange, Collectors.toSet()));
    }

    @Override
    public Map<String, Market> toMarketCodeMap() {
        return markets.stream().collect(Collectors.toMap(Market::getMarketCode, Function.identity()));
    }

    @Override
    public List<Market> toList() {
        return markets.stream().toList();
    }

    @Override
    public Set<Market> toSet() {
        return markets;
    }

    @Override
    public Set<String> toMarketCodeSet() {
        return markets.stream().map(Market::getMarketCode).collect(Collectors.toSet());
    }
}
