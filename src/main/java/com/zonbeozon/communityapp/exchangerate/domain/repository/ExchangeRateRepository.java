package com.zonbeozon.communityapp.exchangerate.domain.repository;

import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findFirstByCodeOrderByUpdatedAtDesc(ExchangeRateCode code);
}
