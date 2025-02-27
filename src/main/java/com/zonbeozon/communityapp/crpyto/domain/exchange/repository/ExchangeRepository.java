package com.zonbeozon.communityapp.crpyto.domain.exchange.repository;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    boolean existsByEnglishName(String englishName);
    Optional<Exchange> findByEnglishName(String englishName);
}
