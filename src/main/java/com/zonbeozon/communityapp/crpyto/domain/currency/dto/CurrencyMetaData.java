package com.zonbeozon.communityapp.crpyto.domain.currency.dto;

import lombok.Builder;

@Builder
public record CurrencyMetaData(
        String englishName,
        String symbol,
        String logo,
        String englishDescription,
        String url
) {
}
