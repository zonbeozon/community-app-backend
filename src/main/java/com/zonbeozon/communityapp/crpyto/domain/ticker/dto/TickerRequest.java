package com.zonbeozon.communityapp.crpyto.domain.ticker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TickerRequest {
    private String marketCode;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double tradePrice;
    private Double signedChangePrice;
    private Double signedChangeRate;
    private Double accTradePrice;
    private LocalDateTime updatedAt;
}
