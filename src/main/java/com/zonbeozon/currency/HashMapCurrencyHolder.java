package com.zonbeozon.currency;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HashMapCurrencyHolder implements CurrencyHolder {
    private final Map<String, Currency> currencies;

    public HashMapCurrencyHolder(List<Currency> currencies) {
        this.currencies = new HashMap<>();
        currencies.forEach(this::add);
    }
    @Override
    public void add(Currency currency) {
        String symbol = currency.getSymbol();
        if(currencies.containsKey(symbol)) {
            throw new IllegalArgumentException("Currency with symbol " + symbol + " already exists");
        }
        currencies.put(currency.getSymbol(), currency);
    }

    @Override
    public Object applyAll(Consumer<Currency> consumer) {
        currencies.values().forEach(consumer);
        return null;
    }

    @Override
    public void apply(String symbol, Consumer<Currency> consumer) {
        consumer.accept(
                Optional.ofNullable(currencies.get(symbol))
                        .orElseThrow(() -> new IllegalArgumentException(symbol + " not found"))
        );
    }

    @Override
    public CurrencyFetchContext getContext() {
        return new CurrencyFetchContext(currencies.values().stream().map(Currency::getSymbol).collect(Collectors.toSet()));
    }
}
