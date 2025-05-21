package com.zonbeozon.currency.service.dto;

import com.zonbeozon.currency.entity.CurrencyFiatMetric;
import com.zonbeozon.fiat.entity.FiatType;

import java.math.BigDecimal;

public record CurrencyFiatMetricResponse(
        FiatType fiatType,
        BigDecimal marketCap,
        BigDecimal fullyDilutedMarketCap,
        BigDecimal volume
) {
    public static CurrencyFiatMetricResponse from(CurrencyFiatMetric currencyFiatMetric) {
        return new CurrencyFiatMetricResponse(
                currencyFiatMetric.getFiatType(),
                currencyFiatMetric.getMarketCap(),
                currencyFiatMetric.getFullyDilutedMarketCap(),
                currencyFiatMetric.getVolume()
        );
    }
}
