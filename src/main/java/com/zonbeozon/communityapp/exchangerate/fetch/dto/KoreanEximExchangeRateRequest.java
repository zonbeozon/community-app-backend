package com.zonbeozon.communityapp.exchangerate.fetch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record KoreanEximExchangeRateRequest(
        @JsonProperty("deal_bas_r")
        @NotBlank(message = "rate cannot be blank")
        String rate,

        @JsonProperty("cur_unit")
        @NotBlank(message = "code cannot be blank")
        String code
) {
}
