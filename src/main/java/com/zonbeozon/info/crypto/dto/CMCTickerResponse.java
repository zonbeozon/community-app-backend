package com.zonbeozon.info.crypto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

/**
 * https://coinmarketcap.com/api/documentation/v1/#operation/getV1CryptocurrencyQuotesLatest
 * 에 기반 하여 만들어짐
 */
public record CMCTickerResponse(
        @JsonProperty("data")
        Map<String, CMCQuote> tickerMap
) {
    public record CMCQuote(
    @JsonProperty("symbol")
    String symbol,

    @JsonProperty("cmc_rank")
    Long rank,

    @JsonProperty("circulating_supply")
    BigDecimal circulatingSupply,

    @JsonProperty("total_supply")
    BigDecimal totalSupply,

    @JsonProperty("quote")
    Quotes quote,

    @JsonProperty("last_updated")
    String timestamp //ISO 8601
    ) {
    }

    public record Quotes(
        @JsonProperty("USD")
        QuoteDetail quoteUsd
    ) {
        public record QuoteDetail(
                @JsonProperty("volume_24h")
                BigDecimal volume24h,
                @JsonProperty("market_cap")
                BigDecimal marketCap,
                @JsonProperty("fully_diluted_market_cap")
                BigDecimal fullyDilutedMarketCap,
                @JsonProperty("percent_change_24h")
                BigDecimal percentageChange24h
        ) {
        }
    }

}
