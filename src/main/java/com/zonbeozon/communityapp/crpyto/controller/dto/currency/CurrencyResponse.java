package com.zonbeozon.communityapp.crpyto.controller.dto.currency;

import lombok.Builder;

@Builder
public record CurrencyResponse(
        Long id,
        String koreanName,
        String englishName,
        String symbol,
        String logo,
        String koreanDescription,
        String englishDescription,
        String website,
        Long rank,
        CurrencyStatsInfoResponse currencyStatsInfo
) {
}
