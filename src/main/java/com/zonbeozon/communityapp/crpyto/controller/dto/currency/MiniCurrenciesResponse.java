package com.zonbeozon.communityapp.crpyto.controller.dto.currency;

import lombok.Builder;

import java.util.List;

@Builder
public record MiniCurrenciesResponse(
        List<SingleMiniCurrencyResponse> currencies,
        Integer size
) {
    @Builder
    public record SingleMiniCurrencyResponse(
            Long id,
            String koreanName,
            String englishName,
            String symbol,
            String logo,
            Long rank,
            CurrencyStatsInfoResponse currencyStatsInfo
    ) {
    }
}
