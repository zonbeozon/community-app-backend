package com.zonbeozon.market.loader;

import com.zonbeozon.market.entity.Market;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ToString(of = "marketRegistries")
class MarketRegistryHolder {
    private final List<MarketRegistry> marketRegistries;

    public MarketRegistryHolder() {
        marketRegistries = new ArrayList<>();
    }

    public void add(MarketRegistry marketRegistry) {
        marketRegistries.add(marketRegistry);
    }

    public void applyAll(Consumer<MarketRegistry> consumer) {
        marketRegistries.forEach(consumer);
    }

    public List<Market> toEntity() {
        return marketRegistries.stream().map(registry -> Market.create(
                registry.getMarketCode(),
                registry.getExchange(),
                registry.getMarketType(),
                registry.getCurrency()
        )).toList();
    }
}
