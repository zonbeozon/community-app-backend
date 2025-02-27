package com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

/**
 * https://coinmarketcap.com/api/documentation/v1/#operation/getV1CryptocurrencyInfo
 * 에 기반하여 만들어짐
 */
public record CMCCurrencyMetaDataRequest(
        @JsonProperty("data")
        @NotEmpty(message = "currency metaData cannot be empty")
        Map<String, CMCCurrencyMetaData> currencyMetaDataMap
        ) {

        public record CMCCurrencyMetaData(
                @JsonProperty("name")
                @NotBlank(message = "english name cannot be blank")
                String englishName,

                @JsonProperty("symbol")
                @NotBlank(message = "symbol cannot be blank")
                String symbol,

                @JsonProperty("logo")
                @NotBlank(message = "logo cannot be blank")
                String logo,

                @JsonProperty("description")
                @NotBlank(message = "english description cannot be blank")
                String englishDescription,

                @JsonProperty("urls")
                CMCUrls urls
        ) {
        }

        public record CMCUrls (
                @JsonProperty("website")
                @Min(value = 1, message = "at least one website needed")
                List<String> website
        ) {
        }
}


