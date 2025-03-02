package com.zonbeozon.communityapp.crpyto.controller.dto.exchange;

import lombok.Builder;

import java.util.List;

@Builder
public record ExchangeResponseWrapper(
        List<ExchangeResponse> exchanges,
        Integer size
) {
    @Builder
    public record ExchangeResponse(
            Long id,
            String koreanName,
            String englishName,
            String englishDescription,
            String logo
    ) {}
}
