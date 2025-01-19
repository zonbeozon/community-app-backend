package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.MarketService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class MarketServiceTest {
    private MarketService marketService;
    private ExchangeMarketService mockExchangeMarketService;
    private MarketRepository mockMarketRepository;
    private ExchangeService mockExchangeService;
    private Market market_1;
    private Market market_2;
    private Exchange exchange;

    @BeforeEach
    void setUp() {
        mockExchangeMarketService = mock(ExchangeMarketService.class);
        mockMarketRepository = mock(MarketRepository.class);
        mockExchangeService = mock(ExchangeService.class);
        marketService = new MarketService(mockMarketRepository, mockExchangeMarketService, mockExchangeService);
        market_1 = ExampleObjectFactory.createExampleMarket(
                "BTC-USDT",
                "비트코인",
                "bitcoin"
        );
        market_2 = ExampleObjectFactory.createExampleMarket(
                "ETH-USDT",
                "이더리움",
                "ethereum"
        );

        exchange = ExampleObjectFactory.createExampleExchange(
                "exampleExchange1",
                "예시거래소1",
                "예시거래소1 입니다."
        );
    }

    @Test
    void 마켓_추가시_동일한_마켓코드를_가진_마켓은_추가되면_안된다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1,market_2));
        MarketFetchResult mfr = new MarketFetchResult();
        mfr.setMarkets(List.of(market_1,market_2));
        mfr.setExchangeName(exchange.getEnglishName());

        marketService.addMarkets(mfr);

        verify(mockMarketRepository, never()).saveAll(any());
        verify(mockExchangeMarketService, never()).add(any(), any());
    }

    @Test
    void 마켓_추가시_동일한_마켓코드가_아니라면_추가한다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1));
        MarketFetchResult mfr = new MarketFetchResult();
        mfr.setMarkets(List.of(market_2));
        mfr.setExchangeName(exchange.getEnglishName());

        marketService.addMarkets(mfr);

        verify(mockMarketRepository, times(1)).saveAll(eq(List.of(market_2)));
        verify(mockExchangeMarketService, times(1)).add(market_2, exchange);
    }
}
