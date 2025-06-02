package com.zonbeozon.market.update;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.*;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.service.MarketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MarketUpdaterTest {
    private static final MarketTestDataProvideRouter basicTestDataProvider = MarketTestDataProvideRouter.withBasicHandler();
    private static final MarketFetchTestDataProvideRouter fetchTestDataProvider = new MarketFetchTestDataProvideRouter();

    @Mock
    private FetchManager<MarketFetchContext, MarketFetchResultWrapper> fetchManager;
    @Mock
    private MarketService marketService;

    @DisplayName("등록되어 있지 않다면 추가한다.")
    @Test
    void shouldAddMarketIfNotRegistered() {
        MultiExchangeMarketHolder multiExchangeMarketHolder = getBasicMarketHolder();
        MarketUpdater marketUpdater = new DefaultMarketUpdater(fetchManager, () -> multiExchangeMarketHolder, marketService);
        MarketFetchResult marketFetchResult = fetchTestDataProvider.getMarketFetchResult(Exchange.UPBIT);
        MarketFetchResultWrapper marketFetchResultWrapper = new MarketFetchResultWrapper(List.of(marketFetchResult));
        Mockito.when(fetchManager.fetch(multiExchangeMarketHolder)).thenReturn(marketFetchResultWrapper);
        marketUpdater.update();
        Mockito.verify(marketService, Mockito.times(marketFetchResult.data().size())).addMarketFiatMetric(Mockito.any(), Mockito.any());
    }

    private MultiExchangeMarketHolder getBasicMarketHolder() {
        List<Market> markets = new ArrayList<>(basicTestDataProvider.getMarkets(Exchange.UPBIT));
        return new HashmapMultiExchangeMarketHolder(markets);
    }
}
