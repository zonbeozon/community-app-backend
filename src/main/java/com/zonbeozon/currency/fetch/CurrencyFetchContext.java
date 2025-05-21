package com.zonbeozon.currency.fetch;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class CurrencyFetchContext {
    private final Set<String> symbols;

    public void add(String symbol) {
        symbols.add(symbol);
    }
}
