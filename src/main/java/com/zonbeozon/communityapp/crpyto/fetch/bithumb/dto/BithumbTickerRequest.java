package com.zonbeozon.communityapp.crpyto.fetch.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BithumbTickerRequest {
    @NotBlank(message = "Market code cannot be blank")
    @JsonProperty("market")
    private String marketCode;
    @NotNull(message = "opening_price cannot be null")
    @JsonProperty("opening_price")
    private Double openingPrice;
    @NotNull(message = "high_price cannot be null")
    @JsonProperty("high_price")
    private Double highPrice;
    @NotNull(message = "Low price cannot be null")
    @JsonProperty("low_price")
    private Double lowPrice;
    @NotNull(message = "Trade price cannot be null")
    @JsonProperty("trade_price")
    private Double tradePrice;
    @NotNull(message = "Signed change price cannot be null")
    @JsonProperty("signed_change_price")
    private Double signedChangePrice;
    @NotNull(message = "Signed change rate cannot be null")
    @JsonProperty("signed_change_rate")
    private Double signedChangeRate;
    @NotNull(message = "Accumulated trade price cannot be null")
    @JsonProperty("acc_trade_price")
    private Double accTradePrice;
}
