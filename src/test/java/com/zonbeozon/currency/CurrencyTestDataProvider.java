package com.zonbeozon.currency;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.fetch.CurrencyMetadataFetchResult;
import com.zonbeozon.currency.fetch.CurrencyQuotesFetchResult;

import java.util.List;

public class CurrencyTestDataProvider {
    List<Currency> getCurrencies();
    CurrencyQuotesFetchResult getMarketFetchResult();
    CurrencyMetadataFetchResult getCurrencyMetadataFetchResult();
}
