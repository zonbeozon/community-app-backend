package com.zonbeozon.info.crypto.dto;

import com.zonbeozon.info.crypto.domain.BaseAsset;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CoinTickerDto {
    private String symbol;
    private Long currencyRank;
    private BigDecimal circulatingSupply;
    private BigDecimal totalSupply;
    private Map<BaseAsset, CoinQuoteDto> quotes;

    private LocalDateTime lastUpdated;
}
