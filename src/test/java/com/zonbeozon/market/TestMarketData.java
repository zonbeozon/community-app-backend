package com.zonbeozon.market;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.MarketType;

import java.math.BigDecimal;

public record TestMarketData (
        String marketCode,
        FiatType fiatType,
        MarketType marketType,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal signedChangeRate,
        BigDecimal signedChangePrice,
        BigDecimal accTradePrice
) {
}
