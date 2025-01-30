package com.zonbeozon.communityapp.crpyto.domain.market.dto;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import lombok.*;

@Builder
public record MarketRequest(
        String marketCode,
        String koreanName,
        String englishName,
        MarketType marketType
) {
}
