package com.zonbeozon.market.fetch;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;

import java.math.BigDecimal;

public record MarketFetchData(
        Long marketId,
        FiatType fiatType,
        String marketCode,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal signedChangePrice,
        BigDecimal signedChangeRate,
        BigDecimal accTradePrice
) {
    public static MarketFetchData from(Market market, MarketFetchResponse marketFetchResponse, FiatType fiatType) {
        return new MarketFetchData(
                market.getId(),
                fiatType,
                market.getMarketCode(),
                marketFetchResponse.openingPrice(),
                marketFetchResponse.highPrice(),
                marketFetchResponse.lowPrice(),
                marketFetchResponse.tradePrice(),
                marketFetchResponse.signedChangePrice(),
                marketFetchResponse.signedChangeRate(),
                marketFetchResponse.accTradePrice()
        );
    }

    public MarketFiatMetric toMarketFiatMetric(Market market) {
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
