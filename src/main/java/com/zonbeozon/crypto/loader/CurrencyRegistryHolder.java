package com.zonbeozon.crypto.loader;

import com.zonbeozon.crypto.entity.Currency;

import java.util.List;
import java.util.function.Consumer;

public interface CurrencyRegistryHolder {
    void add(CurrencyRegistry currencyRegistry);
    void applyAll(Consumer<CurrencyRegistry> consumer);
    void apply(String symbol, Consumer<CurrencyRegistry> consumer);
    Currency toEntity();
    List<CurrencyRegistry> getAll();
}