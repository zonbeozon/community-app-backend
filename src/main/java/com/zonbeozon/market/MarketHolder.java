package com.zonbeozon.market;

import com.zonbeozon.common.fetch.FetchContextSupplier;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.fetch.MarketFetchContext;

import java.util.List;
import java.util.function.Consumer;

public interface MarketHolder extends FetchContextSupplier<MarketFetchContext> {
    void add(Market market);
    void applyAll(Consumer<Market> consumer);
    void applyInExchange(Exchange exchange, Consumer<List<Market>> consumer);
    void apply(Exchange exchange, String marketCode, Consumer<Market> consumer);

}
