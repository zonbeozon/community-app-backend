package com.zonbeozon.currency.repository;

import com.zonbeozon.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CurrencyRepositoryCustom {
    List<Currency> findBySymbolIn(List<String> symbols);
    Optional<Currency> findBySymbol(String symbol);
}
