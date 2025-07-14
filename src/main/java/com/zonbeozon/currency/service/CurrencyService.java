package com.zonbeozon.currency.service;

import com.zonbeozon.global.EntityValidator;
import com.zonbeozon.currency.service.dto.CurrenciesOverviewResponse;
import com.zonbeozon.currency.service.dto.CurrencyResponse;
import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.exception.CurrencyNotFoundException;
import com.zonbeozon.currency.repository.CurrencyRepository;
import com.zonbeozon.fiat.entity.FiatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final EntityValidator entityValidator;

    @Transactional
    public void addCurrencies(List<Currency> currencies) {
        entityValidator.validate(currencies);
        List<Currency> filteredCurrencies = filterDuplicateCurrenciesBySymbol(currencies);
        currencyRepository.saveAll(filteredCurrencies);
    }

    private List<Currency> filterDuplicateCurrenciesBySymbol(List<Currency> currencies) {
        Set<String> duplicateSymbols = currencyRepository.findBySymbolIn(getSymbols(currencies)).stream()
                .map(Currency::getSymbol)
                .collect(Collectors.toCollection(HashSet::new));
        return currencies.stream()
                .filter(currency -> !duplicateSymbols.contains(currency.getSymbol()))
                .toList();
    }

    private List<String> getSymbols(List<Currency> currencies) {
        return currencies.stream().map(Currency::getSymbol).toList();
    }

    @Transactional
    public void deleteCurrency(Long currencyId) {
        Currency currency = getByIdOrThrow(currencyId);
        currencyRepository.delete(currency);
    }

    @Transactional(readOnly = true)
    public Currency getByIdOrThrow(Long currencyId) {
        return currencyRepository.findById(currencyId)
                .orElseThrow(() -> new CurrencyNotFoundException(currencyId + "는 존재하지 않는 currencyId 입니다."));
    }

    @Transactional(readOnly = true)
    public Currency getBySymbolOrThrow(String symbol) {
        return currencyRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CurrencyNotFoundException(symbol + "는 존재하지 않는 symbol 입니다."));
    }

    @Transactional(readOnly = true)
    public CurrencyResponse getCurrencyResponse(Long currencyId, FiatType fiatType) {
        Currency currency = getByIdOrThrow(currencyId);
        return CurrencyResponse.from(currency, fiatType);
    }

    @Transactional(readOnly = true)
    public CurrenciesOverviewResponse getCurrenciesOverviewResponse(FiatType fiatType) {
        List<Currency> currencies = currencyRepository.findAll();
        return CurrenciesOverviewResponse.from(currencies, fiatType);
    }

    @Transactional(readOnly = true)
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}


