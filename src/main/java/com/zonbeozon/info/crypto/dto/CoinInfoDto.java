package com.zonbeozon.info.crypto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CoinInfoDto(
        String symbol,
        String logo,
        LocalizedCoinInfoDto localizedInfo,
        String website,
        LocalDateTime metadataLastUpdated,

        Long currencyRank,
        BigDecimal circulatingSupply,
        BigDecimal totalSupply,
        CoinQuoteDto quote,
        LocalDateTime tickerLastUpdated,

        Long chattingGroupId
) {
}
