package com.zonbeozon.crypto.dto;

import com.zonbeozon.fiat.entity.FiatType;

import java.math.BigDecimal;

public record CurrencyQuotesDto (
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
