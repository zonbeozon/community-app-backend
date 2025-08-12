package com.zonbeozon.crypto.entity;

import com.zonbeozon.fiat.entity.FiatType;
import jakarta.persistence.Embeddable;

@Embeddable
public record CurrencyQuoteId(
        Long currencyId,
        FiatType fiatType
) {
}
