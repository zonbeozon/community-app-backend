package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.fetch.MarketFetchContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class HashmapMarketHolder implements MarketHolder {
    /**
     * Exchange, MarketCode를 KEY로 가지는 Map
     * Exchange, MarketCode 페어는 Market 간 유니크한 값이다.
     */
    private final Map<Exchange, Map<String, Market>> map;

    public HashmapMarketHolder() {
        map = new HashMap<>();
    }

    public HashmapMarketHolder(List<Market> markets) {
        this();
        markets.forEach(this::add);
    }

    @Override
    public void add(Market market) {
        map.computeIfAbsent(market.getExchange(), k -> new HashMap<>())
                .put(market.getMarketCode(), market);
    }

    @Override
    public void applyAll(Consumer<Market> consumer) {
        get().forEach(consumer);
    }

    @Override
    public void applyInExchange(Exchange exchange, Consumer<List<Market>> consumer) {
        consumer.accept(
                getByExchange(exchange).values().stream().toList()
        );
    }

    @Override
    public void apply(Exchange exchange, String marketCode, Consumer<Market> consumer) {
        consumer.accept(
                Optional.ofNullable(getByExchange(exchange).get(marketCode))
                        .orElseThrow(() -> new IllegalStateException("해당 exchange, marketCode를 가지는 market이 등록되지 않았습니다."))
        );
    }

    private Map<String, Market> getByExchange(Exchange exchange) {
        return Optional.ofNullable(map.get(exchange))
                .orElseThrow(() -> new IllegalStateException("해당 exchange를 가지는 market이 등록되지 않았습니다."));
    }

    private List<Market> get() {
        return map.values().stream()
                .flatMap(innerMap -> innerMap.values().stream())
                .toList();
    }

    @Override
    public MarketFetchContext getContext() {
        MarketFetchContext marketFetchContext = new MarketFetchContext();
        map.forEach((exchange, MarketCodeMap) -> marketFetchContext.put(exchange, MarketCodeMap.keySet()));
        return marketFetchContext;
    }
}
