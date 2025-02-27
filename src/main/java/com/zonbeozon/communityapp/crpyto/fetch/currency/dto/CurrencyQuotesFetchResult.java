package com.zonbeozon.communityapp.crpyto.fetch.currency.dto;

import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyQuote;

import java.util.List;

public record CurrencyQuotesFetchResult(
        List<CurrencyQuote> currencyQuotes
) {
}
