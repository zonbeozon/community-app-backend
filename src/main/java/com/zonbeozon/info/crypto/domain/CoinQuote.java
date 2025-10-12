package com.zonbeozon.info.crypto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CoinQuote {
    private BigDecimal marketCap;
    private BigDecimal fullyDilutedMarketCap;
    private BigDecimal volume24h;
}
