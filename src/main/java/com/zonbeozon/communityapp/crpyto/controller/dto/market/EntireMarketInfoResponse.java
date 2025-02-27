package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import lombok.Builder;

import java.util.List;

@Builder
public record EntireMarketInfoResponse(
        Integer size,
        List<MiniMarketInfoResponse> currencies
) {
}
