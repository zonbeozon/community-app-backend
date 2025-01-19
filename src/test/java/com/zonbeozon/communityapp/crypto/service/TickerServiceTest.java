package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.TickerService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class TickerServiceTest {
    private TickerService tickerService;
    private TickerRepository mockTickerRepository;
    private ExchangeService mockExchangeService;
    private ExchangeMarketService mockExchangeMarketService;

    private Exchange exchange;

    private Market market_1;
    private Market market_2;

    private Ticker newTicker_1;
    private Ticker newTicker_2;
    private Ticker oldTicker_1;
    private Ticker oldTicker_2;

    @BeforeEach
    void setup() {
        mockExchangeService = mock(ExchangeService.class);
        mockExchangeMarketService = mock(ExchangeMarketService.class);
        mockTickerRepository = mock(TickerRepository.class);

        tickerService = new TickerService(mockTickerRepository, mockExchangeService, mockExchangeMarketService);

        exchange = ExampleObjectFactory.createExampleExchange(
                "exampleExchange",
                "예시거래소",
                "예시거래소 입니다."
        );
        market_1 = ExampleObjectFactory.createExampleMarket(
                1L,
                "BTC-USDT",
                "비트코인",
                "bitcoin"
        );
        market_2 = ExampleObjectFactory.createExampleMarket(
                2L,
                "ETH-USDT",
                "이더리움",
                "ethereum"
        );
        newTicker_1 = ExampleObjectFactory.createExampleTicker(
                "BTC-USDT",
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                LocalDateTime.of(2025,1,1,0,0)
        );
        newTicker_2 = ExampleObjectFactory.createExampleTicker(
                "ETH-USDT",
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                LocalDateTime.of(2025,1,1,0,0)
        );
        oldTicker_1 = ExampleObjectFactory.createExampleTicker(
                "BTC-USDT",
                999.,
                999.,
                999.,
                999.,
                999.,
                999.,
                999.,
                LocalDateTime.of(2002,1,13,0,0)
        );
        oldTicker_2 = ExampleObjectFactory.createExampleTicker(
                "ETH-USDT",
                999.,
                999.,
                999.,
                999.,
                999.,
                999.,
                999.,
                LocalDateTime.of(2002,1,13,0,0)
        );
    }

    @Test
    void 티커가_신규로_저장된다면_거래소와_마켓과_연관관계가_설정되어야_한다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1, market_2));
        TickerFetchResult tickerFetchResult = createExampleTickerFetchResult();

        tickerService.updateTickers(tickerFetchResult);

        Assertions.assertEquals(market_1.getTicker(), newTicker_1);
        Assertions.assertEquals(market_2.getTicker(), newTicker_2);
    }

    @Test
    void 티커가_신규로_저장된다면_리포지토리에_save메서드를_통해_저장되어야_한다() {
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1, market_2));
        TickerFetchResult tickerFetchResult = createExampleTickerFetchResult();

        tickerService.updateTickers(tickerFetchResult);

        verify(mockTickerRepository, times(2)).save(any(Ticker.class));
    }

    @Test
    void 이미_마켓에_티커가_등록되어_있다면_기존_티커를_업데이트_한다() {
        market_1.setTicker(oldTicker_1);
        market_2.setTicker(oldTicker_2);
        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketService.getMarketsByExchange(exchange)).thenReturn(List.of(market_1, market_2));
        TickerFetchResult tickerFetchResult = createExampleTickerFetchResult();

        tickerService.updateTickers(tickerFetchResult);

        assertEqualTicker(oldTicker_1, newTicker_1);
        assertEqualTicker(oldTicker_2, newTicker_2);
    }

    private TickerFetchResult createExampleTickerFetchResult() {
        TickerFetchResult tickerFetchResult = new TickerFetchResult();
        tickerFetchResult.setTickers(List.of(newTicker_1, newTicker_2));
        tickerFetchResult.setExchangeName(exchange.getEnglishName());
        return tickerFetchResult;
    }

    private void assertEqualTicker(Ticker ticker_1, Ticker ticker_2) {
        Assertions.assertEquals(ticker_1.getAccTradePrice(), ticker_2.getAccTradePrice());
        Assertions.assertEquals(ticker_1.getHighPrice(), ticker_2.getHighPrice());
        Assertions.assertEquals(ticker_1.getLowPrice(), ticker_2.getLowPrice());
        Assertions.assertEquals(ticker_1.getSignedChangePrice(), ticker_2.getSignedChangePrice());
        Assertions.assertEquals(ticker_1.getOpeningPrice(), ticker_2.getOpeningPrice());
        Assertions.assertEquals(ticker_1.getSignedChangeRate(), ticker_2.getSignedChangeRate());
        Assertions.assertEquals(ticker_1.getUpdatedAt(), ticker_2.getUpdatedAt());
    }
}
