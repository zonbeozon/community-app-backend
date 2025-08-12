package com.zonbeozon.crypto.fetcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

/**
 * https://coinmarketcap.com/api/documentation/v1/#operation/getV1CryptocurrencyInfo
 * 에 기반하여 만들어짐
 */
record CMCMetadataResponse(
        @JsonProperty("data")
        @NotEmpty(message = "currency metaData cannot be empty")
        @Valid
        Map<String, CMCMetadata> metadataMap
        ) {
        public record CMCMetadata(
                @JsonProperty("name")
                @NotBlank(message = "name cannot be blank")
                String name,

                @JsonProperty("symbol")
                @NotBlank(message = "symbol cannot be blank")
                String symbol,

                @JsonProperty("logo")
                @NotBlank(message = "logo cannot be blank")
                String logo,

                @JsonProperty("description")
                @NotBlank(message = "description cannot be blank")
                String description,

                @JsonProperty("urls")
                @NotNull
                @Valid
                Urls urls
        ) {
        }

        public record Urls (
                @JsonProperty("website")
                @Size(min = 1, message = "at least one website needed")
                List<String> websites
        ) {
        }
}


