package com.zonbeozon.market.fetch;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;

import java.math.BigDecimal;

public record MarketFetchData(
        String marketCode,
        FiatType fiatType,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal signedChangePrice,
        BigDecimal signedChangeRate,
        BigDecimal accTradePrice
) {
    public MarketFiatMetric createMarketFiatMetric(Market market) {
        return MarketFiatMetric.create(
                market,
                fiatType,
                openingPrice,
                highPrice,
                lowPrice,
                tradePrice,
                signedChangePrice,
                accTradePrice
        );
    }
}
