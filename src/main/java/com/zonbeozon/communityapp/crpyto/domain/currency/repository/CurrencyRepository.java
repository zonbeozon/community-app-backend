package com.zonbeozon.communityapp.crpyto.domain.currency.repository;

import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    boolean existsBySymbol(String symbol);
    Optional<Currency> findByEnglishName(String englishName);
}
