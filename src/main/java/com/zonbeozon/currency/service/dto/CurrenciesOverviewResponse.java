package com.zonbeozon.currency.service.dto;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.fiat.entity.FiatType;

import java.util.List;

public record CurrenciesOverviewResponse(
        int size,
        List<SimplifiedCurrencyResponse> currencies
) {
    public static CurrenciesOverviewResponse from(List<Currency> currencies, FiatType fiatType) {
        return new CurrenciesOverviewResponse(
                currencies.size(),
                currencies.stream().map(currency -> SimplifiedCurrencyResponse.from(currency, fiatType)).toList()
        );
    }
}
