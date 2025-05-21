package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MarketFetchResult {
    private final Exchange exchange;
    private final List<MarketFetchData> data;
}
