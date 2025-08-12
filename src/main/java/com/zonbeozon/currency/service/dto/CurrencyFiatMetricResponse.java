package com.zonbeozon.currency.service.dto;

import com.zonbeozon.crypto.entity.CurrencyQuote;
import com.zonbeozon.fiat.entity.FiatType;

import java.math.BigDecimal;

public record CurrencyFiatMetricResponse(
        FiatType fiatType,
        BigDecimal marketCap,
        BigDecimal fullyDilutedMarketCap,
        BigDecimal volume
) {
    public static CurrencyFiatMetricResponse from(CurrencyQuote currencyQuote) {
        return new CurrencyFiatMetricResponse(
                currencyQuote.getFiatType(),
                currencyQuote.getMarketCap(),
                currencyQuote.getFullyDilutedMarketCap(),
                currencyQuote.getVolume()
        );
    }
}
