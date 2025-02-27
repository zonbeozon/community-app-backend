package com.zonbeozon.communityapp.crpyto.fetch.currency;

import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyMetaDataFetchResult;

import java.util.Collection;

public interface CurrencyMetaDataFetcher {
    CurrencyMetaDataFetchResult fetch(Iterable<String> symbols);
}
