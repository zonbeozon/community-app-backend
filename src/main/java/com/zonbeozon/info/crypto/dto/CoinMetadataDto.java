package com.zonbeozon.info.crypto.dto;

import com.zonbeozon.info.crypto.domain.LanguageCode;

import java.time.LocalDateTime;
import java.util.Map;

public record CoinMetadataDto (
    String symbol,
    String logo,
    Map<LanguageCode, LocalizedCoinInfoDto> localizedInfos,
    String website,
    LocalDateTime lastUpdated
) {
}
