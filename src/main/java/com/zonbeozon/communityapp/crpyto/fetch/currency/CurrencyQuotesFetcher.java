package com.zonbeozon.communityapp.crpyto.fetch.currency;

import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyQuotesFetchResult;

public interface CurrencyQuotesFetcher {
    CurrencyQuotesFetchResult fetch(Iterable<String> symbols);
}
