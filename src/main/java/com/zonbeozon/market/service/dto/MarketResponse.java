package com.zonbeozon.market.service.dto;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.entity.MarketType;

import java.math.BigDecimal;
import java.util.List;

public record MarketResponse(
        String marketCode,
        Exchange exchange,
        MarketType marketType,
        BigDecimal signedChangeRate,
        List<MarketFiatMetricResponse> fiatMetrics
) {
    public static MarketResponse from(Market market, List<MarketFiatMetric> fiatMetrics) {
        return new MarketResponse(
                market.getMarketCode(),
                market.getExchange(),
                market.getMarketType(),
                market.getSignedChangeRate(),
                fiatMetrics.stream().map(MarketFiatMetricResponse::from).toList()
        );
    }
}
