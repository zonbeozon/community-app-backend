package com.zonbeozon.market.fetch;

import com.zonbeozon.market.MarketHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

abstract class AbstractMarketFetcherTest {
    private final MarketFetcher fetcher;

    public AbstractMarketFetcherTest(MarketFetcher fetcher) {
        this.fetcher = fetcher;
    }

    protected abstract MarketHolder getMarketHolder();
    protected abstract List<MarketFetchData> expectedReturn();

    @Test
    @DisplayName("주어진 MarketCode로 요청을 하고 응답을 Dto로 변환하여 리턴한다.")
    void fetch_shouldReturnMappedMarketFetchResult_whenValidMarketCodesProvided() {
        MarketHolder marketHolder = getMarketHolder();
        List<MarketFetchData> result = fetcher.fetchAsync(marketHolder).join();
        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(expectedReturn());
    }
}
