package com.zonbeozon.crypto.repository;

import com.zonbeozon.crypto.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    boolean existsBySymbolIn(Set<String> symbols);
    Optional<Currency> findBySymbol(String symbol);
    @Query("SELECT c.symbol FROM Currency c")
    Set<String> findAllSymbols();
}
