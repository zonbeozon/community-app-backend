package com.zonbeozon.communityapp.crpyto.controller.dto.ticker;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TickerResponse(
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal signedChangePrice,
        BigDecimal signedChangeRate,
        BigDecimal accTradePrice
) {
}
