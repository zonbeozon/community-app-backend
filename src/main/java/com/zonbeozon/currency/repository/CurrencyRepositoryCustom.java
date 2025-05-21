package com.zonbeozon.currency.repository;

import com.zonbeozon.currency.entity.Currency;

import java.util.Map;

public interface CurrencyRepositoryCustom {
    Map<String, Currency> findAllAsMap();
}
