package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.MarketService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTest {
    @Mock
    private ExchangeMarketService mockExchangeMarketService;
    @Mock
    private MarketRepository mockMarketRepository;
    @Mock
    private ExchangeService mockExchangeService;
    @InjectMocks
    private MarketService marketService;
    private Market market_1;
    private Market market_2;
    private Exchange exchange;

    @BeforeEach
    void setUp() {
        createMarkets();
        createExchange();
    }

    @Test
    void 마켓_업데이트시_동일한_마켓코드를_가진_마켓은_추가되면_안된다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1,market_2));
        MarketFetchResult mfr = new MarketFetchResult();
        mfr.setMarkets(List.of(market_1,market_2));
        mfr.setExchangeName(exchange.getEnglishName());

        marketService.updateMarkets(mfr);

        verify(mockMarketRepository, never()).saveAll(any());
        verify(mockExchangeMarketService, never()).add(any(), any());
    }

    @Test
    void 마켓_업데이트시_동일한_마켓코드가_아니라면_추가한다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1));
        MarketFetchResult mfr = new MarketFetchResult();
        mfr.setMarkets(List.of(market_2, market_1));
        mfr.setExchangeName(exchange.getEnglishName());

        marketService.updateMarkets(mfr);

        verify(mockMarketRepository, times(1)).saveAll(eq(List.of(market_2)));
        verify(mockExchangeMarketService, times(1)).add(market_2, exchange);
    }

    @Test
    void 마켓_업데이트시_이미_존재하는_마켓이지만_Fetch시에_존재하지_않는다면_marketStatus를_delisted로_변경한다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1));
        MarketFetchResult mfr = new MarketFetchResult();
        mfr.setMarkets(List.of(market_2));
        mfr.setExchangeName(exchange.getEnglishName());

        marketService.updateMarkets(mfr);

        Assertions.assertEquals(MarketStatus.DELISTED, market_1.getMarketStatus());
    }

    private void createMarkets() {
        market_1 = ExampleObjectFactory.createMarket(
                "BTC-USDT",
                "비트코인",
                "bitcoin",
                MarketType.USDT
        );
        market_2 = ExampleObjectFactory.createMarket(
                "ETH-USDT",
                "이더리움",
                "ethereum",
                MarketType.USDT
        );
    }

    private void createExchange() {
        exchange = ExampleObjectFactory.createExchange(
                "exampleExchange",
                "예시거래소",
                "예시거래소 입니다."
        );
    }
}
