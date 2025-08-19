package com.zonbeozon.crypto.dto;

import java.util.Set;

public record CurrencyMetadataDto(
        String symbol,
        Set<CurrencyLocalizedMetadataDto> localizedMetadata,
        String logo,
        String website
) {
}
