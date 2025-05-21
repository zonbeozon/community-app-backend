package com.zonbeozon.fiat.repository;

import com.zonbeozon.fiat.entity.ConversionRate;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {
    Optional<ConversionRate> findFirstByCodeOrderByCreatedAtDesc(ConversionRateCode code);
}
