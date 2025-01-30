package com.zonbeozon.communityapp.crpyto.domain.ticker.dto;

import lombok.*;

@Builder
public record TickerRequest(
        String marketCode,
        Double openingPrice,
        Double highPrice,
        Double lowPrice,
        Double tradePrice,
        Double signedChangePrice,
        Double signedChangeRate,
        Double accTradePrice
) {
}
