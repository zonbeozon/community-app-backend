package com.zonbeozon.communityapp.exchangerate.service;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.domain.repository.ExchangeRateRepository;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.fetch.ExchangeRateFetcher;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateFetcher exchangeRateFetcher;

    @Transactional(readOnly = true)
    public ExchangeRate getLatestExchangeRate(ExchangeRateCode code) {
        return exchangeRateRepository.findFirstByCodeOrderByUpdatedAtDesc(code)
                .orElseThrow(() -> new ExchangeRateException(ErrorCode.EMPTY_EXCHANGE_RATE));
    }

    @Transactional
    public void updateExchangeRate() {
        try {
            LocalDateTime now = LocalDateTime.now();
            ExchangeRateFetchResult exchangeRateFetchResult = exchangeRateFetcher.fetch(now.toLocalDate());
            exchangeRateFetchResult.exchangeRates().forEach(
                    exchangeRateDto -> {
                        ExchangeRate exchangeRate = getLatestExchangeRate(exchangeRateDto.code());
                        exchangeRate.update(now, exchangeRateDto.rate());
                    }
            );
        } catch (ExchangeRateException e) {
            //휴장일이기 때문에 업데이트 하지 않고 종료
            if(e.getErrorCode().equals(ErrorCode.EXCHANGE_RATE_NOT_AVAILABLE)) return;
            throw e;
        }
    }

    public static BigDecimal calculateOtherCurrencyToKrw(BigDecimal amount, ExchangeRate exchangeRate) {
        return amount.multiply(exchangeRate.getRate());
    };

    public static BigDecimal calculateKrwToOtherCurrency(BigDecimal amount, ExchangeRate exchangeRate) {
        return amount.divide(exchangeRate.getRate(), 2 , BigDecimalUtils.ROUNDING_MODE);
    }

    @PostConstruct
    @Transactional
    public void insertDummy() {
        exchangeRateRepository.save(new ExchangeRate(
                LocalDateTime.now(),
                BigDecimalUtils.createBigDecimal("1000", BigDecimalUtils.FIAT_SCALE),
                ExchangeRateCode.USD)
        );
    }
}
