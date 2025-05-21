package com.zonbeozon.currency;

import com.zonbeozon.common.fetch.FetchContextSupplier;
import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;

import java.util.function.Consumer;

public interface CurrencyHolder extends FetchContextSupplier<CurrencyFetchContext> {
    void add(Currency currency);
    Object applyAll(Consumer<Currency> consumer);
    void apply(String symbol, Consumer<Currency> consumer);
}
