package com.zonbeozon.currency.fetch;

import com.zonbeozon.fiat.entity.FiatType;

import java.math.BigDecimal;

public record CurrencyQuotesFetchData(
        String symbol,
        FiatType fiatType,
        Long rank,
        BigDecimal circulatingSupply,
        BigDecimal totalSupply,
        BigDecimal volume,
        BigDecimal marketCap,
        BigDecimal fullyDilutedMarketCap
) {
}
