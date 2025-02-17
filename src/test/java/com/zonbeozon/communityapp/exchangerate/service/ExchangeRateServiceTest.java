package com.zonbeozon.communityapp.exchangerate.service;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.domain.dto.ExchangeRateDto;
import com.zonbeozon.communityapp.exchangerate.domain.repository.ExchangeRateRepository;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.fetch.ExchangeRateFetcher;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateFetcher exchangeRateFetcher;
    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    @DisplayName("휴장일이면 업데이트 하지 않고 메서드가 종료된다")
    void updateExchangeRate_whenHoliday_thenDoNothing() {
        when(exchangeRateFetcher.fetch(any(LocalDate.class)))
                .thenThrow(new ExchangeRateException(ErrorCode.EXCHANGE_RATE_NOT_AVAILABLE));
        exchangeRateService.updateExchangeRate();
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));
    }

    @Test
    @DisplayName("정상적으로 fetch가 되면 업데이트한다")
    void updateExchangeRate_whenSuccess_thenUpdateExchangeRate() {
        LocalDateTime oldDate = LocalDateTime.of(2002,1,13,0,0);
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .rate(BigDecimalUtils.createBigDecimal("1000", BigDecimalUtils.FIAT_SCALE))
                .updatedAt(oldDate)
                .code(ExchangeRateCode.USD)
                .build();

        BigDecimal fetchedRate = BigDecimalUtils.createBigDecimal("1400", BigDecimalUtils.FIAT_SCALE);
        List<ExchangeRateDto> exchangeRateDtoList = List.of(ExchangeRateDto.builder()
                .rate(fetchedRate)
                .code(ExchangeRateCode.USD).build());
        when(exchangeRateFetcher.fetch(any(LocalDate.class)))
                .thenReturn(new ExchangeRateFetchResult(exchangeRateDtoList));
        when(exchangeRateService.getLatestExchangeRate(any(ExchangeRateCode.class))).thenReturn(exchangeRate);

        exchangeRateService.updateExchangeRate();

        Assertions.assertEquals(fetchedRate, exchangeRate.getRate());
        Assertions.assertNotEquals(oldDate, exchangeRate.getUpdatedAt());
    }
}
