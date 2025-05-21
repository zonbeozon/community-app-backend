package com.zonbeozon.market.service.dto;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.MarketFiatMetric;

import java.math.BigDecimal;

public record MarketFiatMetricResponse(
        FiatType fiatType,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal signedChangePrice,
        BigDecimal accTradePrice
) {
    public static MarketFiatMetricResponse from(MarketFiatMetric marketFiatMetric) {
        return new MarketFiatMetricResponse(
                marketFiatMetric.getFiatType(),
                marketFiatMetric.getHighPrice(),
                marketFiatMetric.getHighPrice(),
                marketFiatMetric.getLowPrice(),
                marketFiatMetric.getTradePrice(),
                marketFiatMetric.getSignedChangePrice(),
                marketFiatMetric.getAccTradePrice()
        );
    }
}
