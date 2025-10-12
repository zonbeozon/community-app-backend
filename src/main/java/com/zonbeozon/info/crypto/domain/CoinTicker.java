package com.zonbeozon.info.crypto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CoinTicker {
    private String symbol;
    private Long currencyRank;
    private BigDecimal circulatingSupply;
    private BigDecimal totalSupply;
    private Map<BaseAsset, CoinQuote> quotes;

    private LocalDateTime lastUpdated;
}
