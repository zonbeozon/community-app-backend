package com.zonbeozon.communityapp.exchangerate.fetch;

import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.dto.ExchangeRateDto;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class SpringBootKoreanEximExchangeRateFetcherTest {
    @Autowired
    private KoreaEximExchangeRateFetcher fetcher;

    @Test
    @DisplayName("fetch 호출시 영업일이 아니면 ExchangeRateException 던지거나 영업일이면 올바른 ExchangeRateFetchResult를 받아야한다.")
    void fetch_ShouldReturnValidExchangeRate() {
        try {
            ExchangeRateFetchResult exchangeRateFetchResult = fetcher.fetch();
            ExchangeRateDto exchangeRateDto = exchangeRateFetchResult.exchangeRates().getFirst();
            Assertions.assertNotNull(exchangeRateDto.code());
            Assertions.assertNotNull(exchangeRateDto.rate());
        } catch (ExchangeRateException e) {
            //영업일이 아닐때 -> 정상
            if(e.getErrorCode().equals(ErrorCode.EXCHANGE_RATE_NOT_AVAILABLE)) return;
        }
    }
}
