package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.MarketFetchTestDataProvideRouter;
import com.zonbeozon.market.MarketTestDataProvideRouter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class MarketFetchManagerTest {
    private static final MarketTestDataProvideRouter marketTestDataProvider = MarketTestDataProvideRouter.withBasicHandler();
    private static final MarketFetchTestDataProvideRouter marketFetchTestDataProvider = new MarketFetchTestDataProvideRouter();
    @Mock
    private MarketFetcher upbitMarketFetcher;
    @Mock
    private MarketFetcher binanceMarketFetcher;
    private FetchManager<MarketFetchContext, MarketFetchResultWrapper> fetchManager;

    @BeforeEach
    void setUp() {
        setMockFetcher();
        fetchManager = new MarketFetchManager(Set.of(upbitMarketFetcher, binanceMarketFetcher));
    }

    @DisplayName("요청에 여러 거래소가 있으면 알맞은 Fetcher를 사용하여 여러개 요청을 하고 Wrapper로 감싸서 리턴한다.")
    @Test
    void d() {
        MarketFetchContext marketFetchContext = new MarketFetchContext();
        marketFetchContext.put(Exchange.UPBIT, marketTestDataProvider.getMarketCodes(Exchange.UPBIT));
        marketFetchContext.put(Exchange.BINANCE, marketTestDataProvider.getMarketCodes(Exchange.BINANCE));

        MarketFetchResultWrapper marketFetchResultWrapper = fetchManager.fetch(() -> marketFetchContext);
        Assertions.assertThat(
                marketFetchResultWrapper.getFetchResults()
                        .stream()
                        .map(MarketFetchResult::getExchange)
                        .collect(Collectors.toSet())
        ).containsExactlyInAnyOrder(Exchange.UPBIT, Exchange.BINANCE);
    }

    private void setMockFetcher() {
        Mockito.when(upbitMarketFetcher.fetch(marketTestDataProvider.getMarketCodes(Exchange.UPBIT)))
                .thenReturn(marketFetchTestDataProvider.getMarketFetchResult(Exchange.UPBIT));
        Mockito.when(binanceMarketFetcher.fetch(marketTestDataProvider.getMarketCodes(Exchange.BINANCE)))
                .thenReturn(marketFetchTestDataProvider.getMarketFetchResult(Exchange.BINANCE));
    }
}
