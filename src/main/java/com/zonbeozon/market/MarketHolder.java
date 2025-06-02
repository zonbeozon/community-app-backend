package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MarketHolder {
    Map<Exchange, Set<Market>> toExchangeMap();
    Map<String, Market> toMarketCodeMap();
    List<Market> toList();
    Set<Market> toSet();
    Set<String> toMarketCodeSet();

}
