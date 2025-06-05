package com.zonbeozon.market.fetch;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.entity.Market;

import java.util.List;
import java.util.Optional;

class MarketFetchDataMapper {
    public <T extends MarketFetchResponse> List<MarketFetchData> map(MarketHolder marketHolder, List<T> response, FiatType fiatType) {
        return response.stream().map(item -> {
            Market matched = Optional.ofNullable(marketHolder.toMarketCodeMap().get(item.marketCode()))
                    .orElseThrow(() -> new IllegalArgumentException("Market not found"));
            return MarketFetchData.from(matched, item, fiatType);
        }).toList();
    }
}
