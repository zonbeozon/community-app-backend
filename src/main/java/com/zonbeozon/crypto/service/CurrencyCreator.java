package com.zonbeozon.crypto.service;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyCreator {
    private final CurrencyRepository currencyRepository;

    public void addCurrencies(List<Currency> currencies) {
        List<String> symbolList = currencies.stream().map(Currency::getSymbol).toList();
        Set<String> symbolSet = new HashSet<>(symbolList);
        if(symbolList.size() != symbolSet.size() || hasDuplicateSymbolIn(symbolSet))
            throw new IllegalStateException("duplicate Currency exists");
        currencyRepository.saveAll(currencies);
    }

    private boolean hasDuplicateSymbolIn(Set<String> symbols) {
        return currencyRepository.existsBySymbolIn(symbols);
    }
}
