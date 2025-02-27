package com.zonbeozon.communityapp.crpyto.controller.dto.currency;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CurrencyStatsInfoResponse(
        BigDecimal circulatingSupply,
        BigDecimal totalSupply,
        BigDecimal marketCap,
        BigDecimal fullyDilutedMarketCap,
        BigDecimal volume
) {
}
