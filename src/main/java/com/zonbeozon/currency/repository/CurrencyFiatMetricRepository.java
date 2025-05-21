package com.zonbeozon.currency.repository;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.entity.CurrencyFiatMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyFiatMetricRepository extends JpaRepository<CurrencyFiatMetric, Long> {
    List<CurrencyFiatMetric> findByCurrency(Currency currency);
}
