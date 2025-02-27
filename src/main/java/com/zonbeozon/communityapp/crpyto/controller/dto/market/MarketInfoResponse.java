package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.ticker.TickerResponse;
import lombok.Builder;

import java.util.Map;

@Builder
public record MarketInfoResponse(
        Long currencyId,
        Map<String, Map<String, TickerResponse>> markets
) {
}
