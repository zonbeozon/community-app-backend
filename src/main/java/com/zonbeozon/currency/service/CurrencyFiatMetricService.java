package com.zonbeozon.currency.service;

import com.zonbeozon.crypto.entity.CurrencyQuote;
import com.zonbeozon.global.EntityValidator;
import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.currency.repository.CurrencyFiatMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyFiatMetricService {
    private final EntityValidator entityValidator;
    private final CurrencyFiatMetricRepository currencyFiatMetricRepository;

    @Transactional
    public void addCurrencyFiatMetric(CurrencyQuote currencyQuote) {
        entityValidator.validate(currencyQuote);
        currencyFiatMetricRepository.save(currencyQuote);
    }

    @Transactional(readOnly = true)
    public List<CurrencyQuote> getCurrencyFiatMetricsByCurrency(Currency currency) {
        return currencyFiatMetricRepository.findByCurrency(currency);
    }

}
