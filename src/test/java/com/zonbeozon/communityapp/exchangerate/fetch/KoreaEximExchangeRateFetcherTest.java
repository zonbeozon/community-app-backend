package com.zonbeozon.communityapp.exchangerate.fetch;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.domain.dto.ExchangeRateDto;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.KoreanEximExchangeRateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KoreaEximExchangeRateFetcherTest {
    @Mock
    private DefaultRestFetcher defaultRestFetcher;
    private KoreaEximExchangeRateFetcher koreaEximExchangeRateFetcher;

    @BeforeEach
    void setup() {
        koreaEximExchangeRateFetcher = new KoreaEximExchangeRateFetcher(defaultRestFetcher, "123");
    }

    @Test
    @DisplayName("빈 리스트를 반환하면 예외를 던진다")
    void fetch_whenEmptyList_thenThrowExchangeRateException() {
        when(defaultRestFetcher.fetchWithParam(anyString(), any(), any())).thenReturn(List.of());

        ExchangeRateException exception = Assertions.assertThrows(
                ExchangeRateException.class,
                () -> koreaEximExchangeRateFetcher.fetch());
        Assertions.assertEquals(ErrorCode.EXCHANGE_RATE_NOT_AVAILABLE, exception.getErrorCode());
    }

    @Test
    @DisplayName("정상적으로 fetch 하면 ExchangeRateFetchResult 반환한다")
    void fetch_whenSuccess_thenReturnExchangeRateFetchResult() {
        BigDecimal rate = BigDecimalUtils.createBigDecimal("1500", BigDecimalUtils.FIAT_SCALE);
        KoreanEximExchangeRateRequest request = new KoreanEximExchangeRateRequest(rate.toString(), "USD");
        when(defaultRestFetcher.fetchWithParam(anyString(), any(), any())).thenReturn(List.of(request));

        ExchangeRateFetchResult exchangeRateFetchResult = koreaEximExchangeRateFetcher.fetch();

        Assertions.assertEquals(1, exchangeRateFetchResult.exchangeRates().size());
        ExchangeRateDto exchangeRateDto = exchangeRateFetchResult.exchangeRates().getFirst();
        Assertions.assertEquals(rate, exchangeRateDto.rate());
        Assertions.assertEquals(ExchangeRateCode.USD, exchangeRateDto.code());
    }
}
