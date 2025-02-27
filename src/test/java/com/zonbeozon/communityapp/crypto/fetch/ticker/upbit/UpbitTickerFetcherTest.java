package com.zonbeozon.communityapp.crypto.fetch.ticker.upbit;

import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.upbit.UpbitTickerFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class UpbitTickerFetcherTest {
    @Autowired
    private UpbitTickerFetcher upbitTickerFetcher;

    @Test
    @DisplayName("fetch시 올바른 TickerFetchResult가 반환되어야 한다")
    void d() {
        List<String> marketCodes = List.of("KRW-BTC", "KRW-ETH");
        TickerFetchResult tickerFetchResult = upbitTickerFetcher.fetch(marketCodes);

        Assertions.assertEquals(2, tickerFetchResult.tickers().size());
    }
}
