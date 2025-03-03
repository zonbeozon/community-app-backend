package com.zonbeozon.communityapp.crpyto.fetch.ticker.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BinanceTickerRequest(
        @JsonProperty("symbol")
        @NotBlank(message = "Market code cannot be blank")
        String marketCode,
        @JsonProperty("openPrice")
        @NotNull(message = "open price cannot be null")
        BigDecimal openingPrice,
        @JsonProperty("highPrice")
        @NotNull(message = "high price cannot be null")
        BigDecimal highPrice,
        @JsonProperty("lowPrice")
        @NotNull(message = "Low price cannot be null")
        BigDecimal lowPrice,
        @JsonProperty("lastPrice")
        @NotNull(message = "Trade price cannot be null")
        BigDecimal tradePrice,
        @JsonProperty("priceChange")
        @NotNull(message = "Signed change price cannot be null")
        BigDecimal signedChangePrice,
        @JsonProperty("priceChangePercent")
        @NotNull(message = "Signed change rate cannot be null")
        BigDecimal signedChangeRate,
        @JsonProperty("quoteVolume")
        @NotNull(message = "Accumulated trade price cannot be null")
        BigDecimal accTradePrice

) {}
