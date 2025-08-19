package com.zonbeozon.crypto.loader;


import com.zonbeozon.crypto.dto.CurrencyMetadataDto;
import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.entity.LocalizedCurrencyInfo;
import com.zonbeozon.crypto.fetcher.MetadataFetcher;
import com.zonbeozon.crypto.service.CurrencyCreator;
import com.zonbeozon.crypto.service.CurrencyFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiBasedMetadataLoader implements MetadataLoader {
    private final MetadataFetcher metadataFetcher;
    private final SymbolLoader symbolLoader;
    private final CurrencyCreator currencyCreator;
    private final CurrencyFinder currencyFinder;

    @Override
    public void load() {
        List<String> symbols = symbolLoader.load();
        Set<String> existSymbols = currencyFinder.findAllSymbols();
        Set<String> duplicateSymbolFiltered = symbols.stream()
                .filter(symbol -> !existSymbols.contains(symbol))
                .collect(Collectors.toSet());
        log.debug("{} symbols loaded, {} symbols filtered", symbols.size(), duplicateSymbolFiltered.size() - symbols.size());
        Set<CurrencyMetadataDto> metaDataDtoSet = metadataFetcher.fetch(symbols);
        List<Currency> currencies = metaDataDtoSet.stream().map(
                metadata -> {
                    Currency currency = new Currency(metadata.symbol(), metadata.logo(), metadata.website());
                    List<LocalizedCurrencyInfo> infos = metadata.localizedMetadata().stream()
                            .map(localizedMetadata -> new LocalizedCurrencyInfo(
                                    currency,
                                    localizedMetadata.languageCode(),
                                    localizedMetadata.name(),
                                    localizedMetadata.description()
                            ))
                            .toList();
                    currency.getLocalizedInfo().addAll(infos);
                    return currency;
                }
        ).toList();

        currencyCreator.addCurrencies(currencies);
    }
}
