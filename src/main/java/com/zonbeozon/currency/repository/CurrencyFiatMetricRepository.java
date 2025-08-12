package com.zonbeozon.currency.repository;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.entity.CurrencyQuote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyFiatMetricRepository extends JpaRepository<CurrencyQuote, Long> {
    List<CurrencyQuote> findByCurrency(Currency currency);
}
