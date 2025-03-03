package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import lombok.Builder;

@Builder
public record AdminMarketResponse(
        Long marketId,
        Long exchangeId,
        MarketType marketType,
        String marketCode
) {
}
