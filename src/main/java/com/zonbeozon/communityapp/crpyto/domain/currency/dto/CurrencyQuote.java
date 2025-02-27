package com.zonbeozon.communityapp.crpyto.domain.currency.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CurrencyQuote(
        String symbol,
        Long rank,
        BigDecimal circulatingSupply,
        BigDecimal totalSupply,
        BigDecimal volume,
        BigDecimal marketCap,
        BigDecimal fullyDilutedMarketCap
) {
}
