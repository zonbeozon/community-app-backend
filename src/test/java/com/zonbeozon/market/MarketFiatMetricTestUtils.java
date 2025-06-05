package com.zonbeozon.market;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;

import java.math.BigDecimal;

public class MarketFiatMetricTestUtils {
    public static MarketFiatMetric createMarketFiatMetricWithOneValue(Market market, FiatType fiatType, BigDecimal value) {
        return MarketFiatMetric.create(
                market,
                FiatType.KRW,
                value,
                value,
                value,
                value,
                value,
                value
        );
    }
}
