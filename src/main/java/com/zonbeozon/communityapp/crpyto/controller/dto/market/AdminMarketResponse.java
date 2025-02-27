package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import lombok.Builder;

@Builder
public record AdminMarketResponse(
        Long marketId,
        Long exchangeId,
        String type
) {
}
