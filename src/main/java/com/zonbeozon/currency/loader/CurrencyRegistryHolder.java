package com.zonbeozon.currency.loader;

import com.zonbeozon.global.fetch.FetchContextSupplier;
import com.zonbeozon.global.fetch.FetchResultConsumer;
import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.currency.fetch.CurrencyMetadataFetchResult;

import java.util.List;
import java.util.function.Consumer;

interface CurrencyRegistryHolder extends FetchContextSupplier<CurrencyFetchContext>, FetchResultConsumer<CurrencyMetadataFetchResult> {
    void add(CurrencyRegistry currencyRegistry);
    void applyAll(Consumer<CurrencyRegistry> consumer);
    List<Currency> toEntity();
    void apply(String symbol, Consumer<CurrencyRegistry> consumer);
}