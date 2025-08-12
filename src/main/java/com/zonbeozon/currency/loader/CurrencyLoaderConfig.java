package com.zonbeozon.currency.loader;

import com.zonbeozon.crypto.loader.HashmapCurrencyRegistryHolder;
import com.zonbeozon.global.fetch.FetchManager;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.crypto.fetcher.MetadataFetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CurrencyLoaderConfig {
    private static final String FILE_PATH = "/data/kr-currencies.json";

    private final FetchManager<CurrencyFetchContext, MetadataFetchResult> fetchManager;

    @Bean
    public CurrencyLoader currencyLoader() {
        JsonCurrencyLoader jsonCurrencyLoader = new JsonCurrencyLoader(FILE_PATH, HashmapCurrencyRegistryHolder::new);
        return new MetadataFetchCurrencyLoaderDecorator(jsonCurrencyLoader, fetchManager);
    }
}
