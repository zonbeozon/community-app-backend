package com.zonbeozon.communityapp.crpyto.controller.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CurrencyRequest(
        @JsonProperty("symbol")
        @NotBlank(message = "symbol은 null이거나 공백일 수 없습니다.")
        String symbol,

        @JsonProperty("koreanName")
        @NotBlank(message = "koreanName은 null이거나 공백일 수 없습니다.")
        String koreanName,

        @JsonProperty("markets")
        @NotEmpty(message = "market은 한개 이상 포함되어야 합니다.")
        List<MarketRequest> markets
) {
}
