package com.zonbeozon.info.crypto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CoinMetadata {
    private String symbol;
    private String logo;
    private Map<LanguageCode, LocalizedCoinInfo> localizedInfos;
    private String website;
    private LocalDateTime lastUpdated;
}
