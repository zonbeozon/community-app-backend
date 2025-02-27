package com.zonbeozon.communityapp.crpyto.fetch.ticker.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BithumbTickerRequest(
    @NotBlank(message = "Market code cannot be blank")
    @JsonProperty("market")
    String marketCode,
    @NotNull(message = "opening_price cannot be null")
    @JsonProperty("opening_price")
    BigDecimal openingPrice,
    @NotNull(message = "high_price cannot be null")
    @JsonProperty("high_price")
    BigDecimal highPrice,
    @NotNull(message = "Low price cannot be null")
    @JsonProperty("low_price")
    BigDecimal lowPrice,
    @NotNull(message = "Trade price cannot be null")
    @JsonProperty("trade_price")
    BigDecimal tradePrice,
    @NotNull(message = "Signed change price cannot be null")
    @JsonProperty("signed_change_price")
    BigDecimal signedChangePrice,
    @NotNull(message = "Signed change rate cannot be null")
    @JsonProperty("signed_change_rate")
    BigDecimal signedChangeRate,
    @NotNull(message = "Accumulated trade price cannot be null")
    @JsonProperty("acc_trade_price_24h")
    BigDecimal accTradePrice
) {
        }
