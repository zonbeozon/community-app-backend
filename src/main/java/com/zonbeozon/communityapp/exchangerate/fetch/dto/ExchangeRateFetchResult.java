package com.zonbeozon.communityapp.exchangerate.fetch.dto;

import com.zonbeozon.communityapp.exchangerate.domain.dto.ExchangeRateDto;
import java.util.List;

public record ExchangeRateFetchResult(
        List<ExchangeRateDto> exchangeRates
) {
}
