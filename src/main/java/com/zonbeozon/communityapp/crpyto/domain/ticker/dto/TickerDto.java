package com.zonbeozon.communityapp.crpyto.domain.ticker.dto;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TickerDto(
    String marketCode,
    MarketType marketType,
    BigDecimal openingPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    BigDecimal tradePrice,
    BigDecimal signedChangePrice,
    BigDecimal signedChangeRate,
    BigDecimal accTradePrice
    ) {
}
