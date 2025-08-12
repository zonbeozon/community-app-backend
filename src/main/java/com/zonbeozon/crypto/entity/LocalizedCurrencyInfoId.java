package com.zonbeozon.crypto.entity;

import com.zonbeozon.crypto.enums.LanguageCode;
import jakarta.persistence.Embeddable;

@Embeddable
public record LocalizedCurrencyInfoId(
        Long CurrencyId,
        LanguageCode languageCode
) {
}
