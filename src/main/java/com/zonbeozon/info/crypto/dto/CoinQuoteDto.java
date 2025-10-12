package com.zonbeozon.info.crypto.dto;

import com.zonbeozon.info.crypto.domain.CoinQuote;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CoinQuoteDto {
    private BigDecimal marketCap;
    private BigDecimal fullyDilutedMarketCap;
    private BigDecimal volume24h;

    public static CoinQuoteDto from(CoinQuote coinQuote) {
        return new CoinQuoteDto(coinQuote.getMarketCap(), coinQuote.getFullyDilutedMarketCap(), coinQuote.getVolume24h());
    }
}
