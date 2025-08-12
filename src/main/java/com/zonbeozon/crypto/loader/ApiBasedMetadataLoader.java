package com.zonbeozon.crypto.loader;


import com.zonbeozon.crypto.enums.LanguageCode;
import com.zonbeozon.crypto.fetcher.MetadataFetchResult;
import com.zonbeozon.crypto.fetcher.MetadataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiBasedMetadataLoader implements MetadataLoader {
    private final MetadataFetcher metadataFetcher;
    private final SymbolLoader symbolLoader;

    @Override
    public CurrencyRegistryHolder load() {
        List<String> symbols = symbolLoader.load();
        MetadataFetchResult fetchResult = metadataFetcher.fetch(symbols);
        Set<CurrencyRegistry> registries = fetchResult.getContent().stream().map(
                metadata -> {
                    Map<LanguageCode, String> names = new HashMap<>();
                    names.put(fetchResult.getLanguageCode(), metadata.name());
                    Map<LanguageCode, String> descriptions = new HashMap<>();
                    names.put(fetchResult.getLanguageCode(), metadata.description());
                    return CurrencyRegistry.builder()
                            .symbol(metadata.symbol())
                            .names(names)
                            .descriptions(descriptions)
                            .logo(metadata.logo())
                            .website(metadata.website())
                            .build();
                }
        ).collect(Collectors.toSet());
        return new HashMapCurrencyRegistryHolder(registries);
    }
}
