package com.zonbeozon.communityapp.crpyto.fetch.ticker.dto;

import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;

import java.util.List;

public record TickerFetchResult(
        List<TickerDto> tickers
) {
}
