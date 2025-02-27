package com.zonbeozon.communityapp.crypto.fetch.ticker.bithumb;

import com.zonbeozon.communityapp.crpyto.fetch.ticker.bithumb.strategy.rest.BithumbTickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class BithumbTickerFetcherTest {
    @Autowired
    private BithumbTickerFetcher bithumbTickerFetcher;

    @Test
    @DisplayName("fetch시 올바른 TickerFetchResult가 반환되어야 한다")
    void d() {
        List<String> marketCodes = List.of("KRW-BTC", "KRW-ETH");
        TickerFetchResult tickerFetchResult = bithumbTickerFetcher.fetch(marketCodes);

        Assertions.assertEquals(2, tickerFetchResult.tickers().size());
    }
}
