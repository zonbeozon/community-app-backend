package com.zonbeozon.currency.service;

import com.zonbeozon.global.EntityValidator;
import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.entity.CurrencyFiatMetric;
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
    public void addCurrencyFiatMetric(CurrencyFiatMetric currencyFiatMetric) {
        entityValidator.validate(currencyFiatMetric);
        currencyFiatMetricRepository.save(currencyFiatMetric);
    }

    @Transactional(readOnly = true)
    public List<CurrencyFiatMetric> getCurrencyFiatMetricsByCurrency(Currency currency) {
        return currencyFiatMetricRepository.findByCurrency(currency);
    }

}
