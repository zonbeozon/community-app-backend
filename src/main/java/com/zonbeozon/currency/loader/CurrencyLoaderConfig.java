package com.zonbeozon.currency.loader;

import com.zonbeozon.common.fetch.FetchManager;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.currency.fetch.CurrencyMetadataFetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
class CurrencyLoaderConfig {
    private static final String FILE_PATH = "/data/kr-currencies.json";

    private final FetchManager<CurrencyFetchContext, CurrencyMetadataFetchResult> fetchManager;

    @Bean
    public CurrencyLoader currencyLoader() {
        JsonCurrencyLoader jsonCurrencyLoader = new JsonCurrencyLoader(FILE_PATH, HashmapCurrencyRegistryHolder::new);
        return new MetadataFetchCurrencyLoaderDecorator(jsonCurrencyLoader, fetchManager);
    }
}
