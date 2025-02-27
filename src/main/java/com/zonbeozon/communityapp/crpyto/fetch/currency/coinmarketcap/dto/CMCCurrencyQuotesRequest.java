package com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

/**
 * https://coinmarketcap.com/api/documentation/v1/#operation/getV1CryptocurrencyQuotesLatest
 * 에 기반 하여 만들어짐
 */
public record CMCCurrencyQuotesRequest(
        @JsonProperty("data")
        @NotEmpty(message = "currency quotes cannot be empty")
        Map<String, CMCCurrencyQuote> currencyQuoteMap
) {

    public record CMCCurrencyQuote(
    @JsonProperty("symbol")
    @NotBlank(message = "symbol cannot be blank")
    String symbol,

    @JsonProperty("cmc_rank")
    @NotNull(message = "rank cannot be null")
    Long rank,

    @JsonProperty("circulating_supply")
    @NotNull(message = "circulating supply cannot be null")
    BigDecimal circulatingSupply,

    @JsonProperty("total_supply")
    @NotNull(message = "total supply cannot be null")
    BigDecimal totalSupply,

    @JsonProperty("quote")
    @NotNull(message = "cmc currency quote usd price detail cannot be null")
    CMCCurrencyQuoteUsdPriceDetail cmcCurrencyQuoteUsdPriceDetail
    ) {
    }

    public record CMCCurrencyQuoteUsdPriceDetail(
        @JsonProperty("USD")
        @NotNull(message = "cmc currency quote price detail cannot be null")
        CMCCurrencyQuotePriceDetail cmcCurrencyQuotePriceDetail
    ) {
    }

    public record CMCCurrencyQuotePriceDetail(
            @JsonProperty("volume_24h")
            @NotNull(message = "volume cannot be null")
            BigDecimal volume,

            @JsonProperty("market_cap")
            @NotNull(message = "market cap cannot be null")
            BigDecimal marketCap,

            @JsonProperty("fully_diluted_market_cap")
            @NotNull(message = "fdv cannot be null")
            BigDecimal fullyDilutedMarketCap
    ) {
    }
}
