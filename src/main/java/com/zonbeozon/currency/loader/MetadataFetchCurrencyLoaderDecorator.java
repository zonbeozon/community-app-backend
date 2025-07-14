package com.zonbeozon.currency.loader;

import com.zonbeozon.global.fetch.FetchManager;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.currency.fetch.CurrencyMetadataFetchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MetadataFetchCurrencyLoaderDecorator implements CurrencyLoader {
    private final CurrencyLoader prevCurrencyLoader;
    private final FetchManager<CurrencyFetchContext, CurrencyMetadataFetchResult> fetchManager;

    @Override
    public CurrencyRegistryHolder load() {
        CurrencyRegistryHolder registryHolder = prevCurrencyLoader.load();
        CurrencyMetadataFetchResult metadataFetchResult = fetchManager.fetch(registryHolder);
        registryHolder.consumeFetchResult(metadataFetchResult);
        return registryHolder;
    }
}
