package com.zonbeozon.crypto.loader;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.entity.LocalizedCurrencyInfo;
import com.zonbeozon.crypto.enums.LanguageCode;

import java.util.*;
import java.util.function.Consumer;

class HashMapCurrencyRegistryHolder implements CurrencyRegistryHolder {
    private final Map<String, CurrencyRegistry> currencyRegistries;

    public HashMapCurrencyRegistryHolder() {
        this.currencyRegistries = new HashMap<>();
    }

    public HashMapCurrencyRegistryHolder(Set<CurrencyRegistry> currencyRegistries) {
        this.currencyRegistries = new HashMap<>();
        currencyRegistries.forEach(this::add);
    }

    @Override
    public void add(CurrencyRegistry currencyRegistry) {
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
        currencyRegistries.values().stream()
                .map(registry -> {
                    Map<LanguageCode, LocalizedCurrencyInfo> currencyInfo = Arrays.stream(LanguageCode.values())
                            .map(languageCode -> new LocalizedCurrencyInfo(languageCode,  ))
                            registry.getNames().entrySet().stream().map(entry -> entry.getKey())
                    return new Currency(registry.getSymbol(), registry.getLogo(), registry.getWebsite())
                })
    }
}
