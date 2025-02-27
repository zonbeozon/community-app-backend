package com.zonbeozon.communityapp.exchangerate.domain.dto;

import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeRateDto(
        ExchangeRateCode code,
        BigDecimal rate
) {
}
