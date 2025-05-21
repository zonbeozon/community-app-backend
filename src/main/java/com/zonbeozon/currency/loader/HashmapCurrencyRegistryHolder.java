package com.zonbeozon.currency.loader;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.entity.CurrencyDescription;
import com.zonbeozon.currency.entity.CurrencyName;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.currency.fetch.CurrencyMetadataFetchResult;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class HashmapCurrencyRegistryHolder implements CurrencyRegistryHolder {
    private final Map<String, CurrencyRegistry> currencyRegistries;

    public HashmapCurrencyRegistryHolder() {
        this.currencyRegistries = new HashMap<>();
    }

    @Override
    public void add(CurrencyRegistry currencyRegistry) {
        String symbol = currencyRegistry.getSymbol();
        if(currencyRegistries.containsKey(symbol)) {
            throw new IllegalArgumentException("Currency registry with symbol " + symbol + " already exists");
        }
        currencyRegistries.put(currencyRegistry.getSymbol(), currencyRegistry);
    }

    @Override
    public void applyAll(Consumer<CurrencyRegistry> consumer) {
        currencyRegistries.values().forEach(consumer);
    }

    @Override
    public void apply(String symbol, Consumer<CurrencyRegistry> consumer) {
        consumer.accept(
            Optional.ofNullable(currencyRegistries.get(symbol))
                    .orElseThrow(() -> new IllegalArgumentException(symbol + " not found"))
            );
    }

    @Override
    public List<Currency> toEntity() {
        return currencyRegistries.values().stream()
                .map(registry -> Currency.create(
                        new CurrencyName(registry.getEnName(), registry.getKrName()),
                        new CurrencyDescription(registry.getEnDescription(), registry.getKrDescription()),
                        registry.getLogo(),
                        registry.getSymbol(),
                        registry.getWebsite()
                ))
                .toList();
    }


    @Override
    public CurrencyFetchContext getContext() {
        return new CurrencyFetchContext(currencyRegistries.values().stream().map(CurrencyRegistry::getSymbol).collect(Collectors.toSet()));
    }

    @Override
    public void consumeFetchResult(CurrencyMetadataFetchResult fetchResult) {
        fetchResult.getMetadata().forEach(
                currencyFetchMetaData -> apply(
                        currencyFetchMetaData.symbol(),
                        (currencyRegistry) -> {
                            currencyRegistry.setEnDescription(currencyFetchMetaData.enDescription());
                            currencyRegistry.setEnName(currencyFetchMetaData.enName().toLowerCase());
                            currencyRegistry.setLogo(currencyFetchMetaData.logo());
                            currencyRegistry.setWebsite(currencyFetchMetaData.website());
                        })
        );
    }
}
