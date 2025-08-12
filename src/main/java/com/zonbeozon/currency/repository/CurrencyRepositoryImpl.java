package com.zonbeozon.currency.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.crypto.entity.Currency;
import jakarta.persistence.EntityManager;

import java.util.Map;

public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CurrencyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Map<String, Currency> findAllAsMap() {
        return Map.of();
    }
}
