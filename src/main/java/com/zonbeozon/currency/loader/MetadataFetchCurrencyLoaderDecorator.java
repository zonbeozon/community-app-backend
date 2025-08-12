package com.zonbeozon.currency.loader;

import com.zonbeozon.crypto.loader.CurrencyRegistryHolder;
import com.zonbeozon.global.fetch.FetchManager;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.crypto.fetcher.MetadataFetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MetadataFetchCurrencyLoaderDecorator implements CurrencyLoader {
    private final CurrencyLoader prevCurrencyLoader;
    private final FetchManager<CurrencyFetchContext, MetadataFetchResult> fetchManager;

    @Override
    public CurrencyRegistryHolder load() {
        CurrencyRegistryHolder registryHolder = prevCurrencyLoader.load();
        MetadataFetchResult metadataFetchResult = fetchManager.fetch(registryHolder);
        registryHolder.consumeFetchResult(metadataFetchResult);
        return registryHolder;
    }
}
