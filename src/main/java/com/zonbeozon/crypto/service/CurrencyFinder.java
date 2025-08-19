package com.zonbeozon.crypto.service;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CurrencyFinder {
    private final CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Set<String> findAllSymbols() {
        return currencyRepository.findAllSymbols();
    }



}
