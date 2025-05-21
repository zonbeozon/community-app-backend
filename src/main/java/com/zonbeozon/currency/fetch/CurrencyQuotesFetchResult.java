package com.zonbeozon.currency.fetch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class CurrencyQuotesFetchResult {
    private final List<CurrencyQuotesFetchData> quotesFetchData;
}
