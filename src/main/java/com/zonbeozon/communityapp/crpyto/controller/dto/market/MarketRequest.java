package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record MarketRequest(
        @JsonProperty("exchange")
        @NotBlank(message = "exchange는 null이거나 공백일 수 없습니다.")
        String exchangeName,

        @JsonProperty("pairs")
        @NotEmpty(message = "페어는 한개 이상 포함되어야 합니다.")
        List<@NotBlank(message = "pair는 null이거나 공백일 수 없습니다.") String> pairs
){
}