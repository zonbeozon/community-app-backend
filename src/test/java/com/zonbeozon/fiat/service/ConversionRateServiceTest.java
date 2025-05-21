package com.zonbeozon.fiat.service;

import com.zonbeozon.fiat.entity.ConversionRateCode;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.exception.ConversionRateException;
import com.zonbeozon.fiat.fetch.ConversionRateFetchManager;
import com.zonbeozon.fiat.repository.ConversionRateRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ConversionRateServiceTest {
    @Mock
    private ConversionRateRepository conversionRateRepository;
    @Mock
    private ConversionRateFetchManager conversionRateFetchManager;
    @InjectMocks
    private ConversionRateService conversionRateService;

    @Test
    @DisplayName("지원하지 않는 ConversionRateCode일때 예외발생")
    void givenUnsupportedFiatTypePair_whenGetConversionRate_thenThrowException() {
        Assertions.assertThatThrownBy(() -> conversionRateService.getConversionRate(FiatType.USD, FiatType.USD))
                .isInstanceOf(ConversionRateException.class);
    }

    @Test
    @DisplayName("db에 해당하는 정보가 없을때 예외발생")
    void givenNoConversionRateInDb_whenGetConversionRate_thenThrowException() {
        Mockito.when(conversionRateRepository.findFirstByCodeOrderByCreatedAtDesc(ConversionRateCode.USD_KRW))
                .thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> conversionRateService.getConversionRate(FiatType.USD, FiatType.KRW))
                .isInstanceOf(ConversionRateException.class);
    }
}
