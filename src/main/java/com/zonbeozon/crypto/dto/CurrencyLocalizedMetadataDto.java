package com.zonbeozon.crypto.dto;

import com.zonbeozon.crypto.enums.LanguageCode;

public record CurrencyLocalizedMetadataDto(
        LanguageCode languageCode,
        String name,
        String description
) {
}
