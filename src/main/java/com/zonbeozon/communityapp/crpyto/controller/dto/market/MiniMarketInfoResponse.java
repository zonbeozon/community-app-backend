package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MiniMarketInfoResponse(
        Long currencyId,
        BigDecimal tradePrice,
        BigDecimal signedChangeRate
) {
}
