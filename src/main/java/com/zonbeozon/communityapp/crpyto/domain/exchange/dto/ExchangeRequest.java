package com.zonbeozon.communityapp.crpyto.domain.exchange.dto;

import lombok.Builder;

@Builder
public record ExchangeRequest(
        String englishName,
        String koreanName,
        String description
) {
}
