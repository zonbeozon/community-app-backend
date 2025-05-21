package com.zonbeozon.fiat.service;

import com.zonbeozon.fiat.entity.ConversionRate;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.exception.ConversionRateException;
import com.zonbeozon.fiat.fetch.ConversionRateFetchManager;
import com.zonbeozon.fiat.repository.ConversionRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionRateService {
    private final ConversionRateRepository conversionRateRepository;
    private final ConversionRateFetchManager conversionRateFetchManager;

    @Transactional(readOnly = true)
    public BigDecimal getConversionRate(FiatType from, FiatType to) {
        ConversionRateCode conversionRateCode = ConversionRateCode.parse(from, to);
        return conversionRateRepository.findFirstByCodeOrderByCreatedAtDesc(conversionRateCode)
                .orElseThrow(() -> new ConversionRateException("등록된 환율 정보가 없습니다."))
                .getValue();
    }

    @Transactional
    public void updateConversionRate() {
        List<ConversionRate> conversionRates = Arrays.stream(ConversionRateCode.values())
                .map(conversionRate -> conversionRateFetchManager.fetch(conversionRate).toEntity())
                .toList();
        conversionRateRepository.saveAll(conversionRates);
    }
}
