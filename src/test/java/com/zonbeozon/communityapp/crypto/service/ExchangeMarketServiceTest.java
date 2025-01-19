package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository.ExchangeMarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class ExchangeMarketServiceTest {
    private ExchangeMarketRepository mockExchangeMarketRepository;
    private ExchangeMarketService exchangeMarketService;
    private ExchangeService mockExchangeService;
    private Market market_1;
    private Exchange exchange_1;
    private Market market_2;
    private Exchange exchange_2;

    @BeforeEach
    void setUp() {
        //exchangeMarketRepo mockito setup
        mockExchangeMarketRepository = mock(ExchangeMarketRepository.class);

        //exchangeService mockito setup
        mockExchangeService = mock(ExchangeService.class);

        //exchangeMarketService setup
        exchangeMarketService = new ExchangeMarketService(mockExchangeMarketRepository, mockExchangeService);

        //example entity setup
        market_1 = ExampleObjectFactory.createExampleMarket(
                1L,
                "BTC-USDT",
                "비트코인",
                "bitcoin"
        );

        exchange_1 = ExampleObjectFactory.createExampleExchange(
                1L,
                "exampleExchange1",
                "예시거래소1",
                "예시거래소1 입니다."
        );

        market_2 = ExampleObjectFactory.createExampleMarket(
                2L,
                "ETH-USDT",
                "이더리움",
                "ethereum"
        );

        exchange_2 = ExampleObjectFactory.createExampleExchange(
                2L,
                "exampleExchange2",
                "예시거래소2",
                "예시거래소2 입니다"
        );
    }

    @Test
    void 정상적으로_저장되어야_한다() {
        exchangeMarketService.add(market_1, exchange_1);

        verify(mockExchangeMarketRepository,times(1)).save(any(ExchangeMarket.class));
        Assertions.assertEquals(1, market_1.getExchangeMarkets().size());
        Assertions.assertEquals(1, exchange_1.getExchangeMarkets().size());
    }


    @Test
    void 같은_거래소에서_중복되는_마켓_코드가_있으면_true를_반환한다() {
        ExchangeMarket exchangeMarket = new ExchangeMarket(exchange_1, market_1);

        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange_1)).thenReturn(List.of(exchangeMarket));

        Assertions.assertTrue(exchangeMarketService.isDuplicate(market_1, exchange_1));
    }

    @Test
    void 같은_거래소에서_중복되는_마켓_코드가_없으면_false를_반환한다() {
        ExchangeMarket exchangeMarket = new ExchangeMarket(exchange_1, market_1);

        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange_1)).thenReturn(List.of(exchangeMarket));

        Assertions.assertFalse(exchangeMarketService.isDuplicate(market_2, exchange_2));

    }

    @Test
    void 다른_거래소라면_중복되는_마켓_코드가_있어도_false를_반환한다() {
        ExchangeMarket exchangeMarket_1 = new ExchangeMarket(exchange_1, market_1);
        ExchangeMarket exchangeMarket_2 = new ExchangeMarket(exchange_2, market_2);

        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange_1)).thenReturn(List.of(exchangeMarket_1));
        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange_2)).thenReturn(List.of(exchangeMarket_2));

        Assertions.assertFalse(exchangeMarketService.isDuplicate(market_1, exchange_2));
    }

    @Test
    void 거래소_영문명에_해당하는_거래소의_마켓들을_리턴한다() {
        ExchangeMarket exchangeMarket_1 = new ExchangeMarket(exchange_1, market_1);
        ExchangeMarket exchangeMarket_2 = new ExchangeMarket(exchange_1, market_2);

        when(mockExchangeService.findByName(exchange_1.getEnglishName())).thenReturn(exchange_1);
        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange_1)).thenReturn(List.of(
                exchangeMarket_1,
                exchangeMarket_2));

        List<Market> market = exchangeMarketService.getMarketsByExchangeName(exchange_1.getEnglishName());

        Assertions.assertEquals(2, market.size());
        Assertions.assertEquals(List.of(market_1, market_2), market);
    }
}
