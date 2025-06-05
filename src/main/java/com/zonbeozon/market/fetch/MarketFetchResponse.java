package com.zonbeozon.market.fetch;

import java.math.BigDecimal;

public interface MarketFetchResponse {
    String marketCode();
    BigDecimal openingPrice();
    BigDecimal highPrice();
    BigDecimal lowPrice();
    BigDecimal tradePrice();
    BigDecimal signedChangePrice();
    BigDecimal signedChangeRate();
    BigDecimal accTradePrice();
}
